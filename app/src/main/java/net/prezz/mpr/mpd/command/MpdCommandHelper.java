package net.prezz.mpr.mpd.command;

import java.util.Comparator;

import net.prezz.mpr.model.LibraryEntity;
import net.prezz.mpr.model.UriEntity;
import net.prezz.mpr.model.UriEntity.UriType;

public class MpdCommandHelper {

    private static final UriComparator URI_COMPARATOR = new UriComparator();


    private MpdCommandHelper() {
        //prevent instantiation
    }

    public static Integer getTrack(String track) {
        if (!track.isEmpty()) {
            try {
                int idx = track.indexOf('/');
                return (idx != -1) ? Integer.decode(track.substring(0, idx)) : Integer.decode(track);
            } catch (NumberFormatException ex) {
            }
        }

        return null;
    }

    public static String createQuery(String prefix, LibraryEntity entity) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(prefix);

        if (entity.getArtist() != null) {
            stringBuilder.append(" Artist \"");
            stringBuilder.append(fixQuery(entity.getArtist()));
            stringBuilder.append("\"");
        }

        if (entity.getAlbumArtist() != null) {
            stringBuilder.append(" AlbumArtist \"");
            stringBuilder.append(fixQuery(entity.getAlbumArtist()));
            stringBuilder.append("\"");
        }

        if (entity.getComposer() != null) {
            stringBuilder.append(" Composer \"");
            stringBuilder.append(fixQuery(entity.getComposer()));
            stringBuilder.append("\"");
        }

        if (entity.getAlbum() != null) {
            stringBuilder.append(" Album \"");
            stringBuilder.append(fixQuery(entity.getAlbum()));
            stringBuilder.append("\"");
        }

        if (entity.getGenre() != null) {
            stringBuilder.append(" Genre \"");
            stringBuilder.append(fixQuery(entity.getGenre()));
            stringBuilder.append("\"");
        }

        if (entity.getTitle() != null) {
            stringBuilder.append(" Title \"");
            stringBuilder.append(fixQuery(entity.getTitle()));
            stringBuilder.append("\"");
        }

        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public static String createTrackQuery(String prefix, LibraryEntity entity, String title) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(prefix);

        if (entity.getArtist() != null) {
            stringBuilder.append(" Artist \"");
            stringBuilder.append(fixQuery(entity.getArtist()));
            stringBuilder.append("\"");
        }

        if (entity.getAlbumArtist() != null) {
            stringBuilder.append(" AlbumArtist \"");
            stringBuilder.append(fixQuery(entity.getAlbumArtist()));
            stringBuilder.append("\"");
        }

        if (entity.getComposer() != null) {
            stringBuilder.append(" Composer \"");
            stringBuilder.append(fixQuery(entity.getComposer()));
            stringBuilder.append("\"");
        }

        if (entity.getAlbum() != null) {
            stringBuilder.append(" Album \"");
            stringBuilder.append(fixQuery(entity.getAlbum()));
            stringBuilder.append("\"");
        }

        if (entity.getGenre() != null) {
            stringBuilder.append(" Genre \"");
            stringBuilder.append(fixQuery(entity.getGenre()));
            stringBuilder.append("\"");
        }

        stringBuilder.append(" Title \"");
        stringBuilder.append(fixQuery(title));
        stringBuilder.append("\"");

        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    public static String fixQuery(String input) {
        if (input.contains("\"")) {
            StringBuilder sb = new StringBuilder();
            for (char c : input.toCharArray()) {
                if ('"' == c) {
                    sb.append("\\");
                }
                sb.append(c);
            }

            return sb.toString();
        }
        return input;
    }

    public static Comparator<UriEntity> getUriComparator() {
        return URI_COMPARATOR;
    }

    private static final class UriComparator implements Comparator<UriEntity> {

        @Override
        public int compare(UriEntity lhs, UriEntity rhs) {
            if (lhs.getUriType() == UriType.DIRECTORY && rhs.getUriType() != UriType.DIRECTORY) {
                return -1;
            }
            if (lhs.getUriType() != UriType.DIRECTORY && rhs.getUriType() == UriType.DIRECTORY) {
                return 1;
            }

            return lhs.getUriPath().compareToIgnoreCase(rhs.getUriPath());
        }
    }
}
