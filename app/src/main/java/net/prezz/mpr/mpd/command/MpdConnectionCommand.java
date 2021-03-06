package net.prezz.mpr.mpd.command;

import net.prezz.mpr.model.TaskHandle;
import net.prezz.mpr.model.TaskHandleImpl;
import net.prezz.mpr.mpd.connection.MpdConnection;
import android.os.AsyncTask;
import android.util.Log;

public abstract class MpdConnectionCommand<Param, Result> extends MpdCommand {

    public interface MpdConnectionCommandReceiver<Result> {
        void receive(Result result);
    }

    private Param param;

    public MpdConnectionCommand(Param param) {
        this.param = param;
    }

    @SuppressWarnings("unchecked")
    public final TaskHandle execute(MpdConnection connection, final MpdConnectionCommandReceiver<Result> commandReceiver) {

        AsyncTask<Object, Void, Result> task = new AsyncTask<Object, Void, Result>() {
            @Override
            protected Result doInBackground(Object... params) {
                try {
                    synchronized (lock) {
                        MpdConnection connectionParam = (MpdConnection)params[0];
                        try {
                            connectionParam.connect();
                            return doExecute(connectionParam, (Param)params[1]);
                        } finally {
                            connectionParam.disconnect();
                        }
                    }
                } catch (Exception ex) {
                    Log.e(MpdConnectionCommand.class.getName(), "error executing command", ex);
                    return onError();
                }
            }

            @Override
            protected void onPostExecute(Result result) {
                commandReceiver.receive(result);
            }
        };

        return new TaskHandleImpl<Object, Void, Result>(task.executeOnExecutor(executor, connection, param));
    }

    protected abstract Result doExecute(MpdConnection connection, Param param) throws Exception;

    protected abstract Result onError();
}
