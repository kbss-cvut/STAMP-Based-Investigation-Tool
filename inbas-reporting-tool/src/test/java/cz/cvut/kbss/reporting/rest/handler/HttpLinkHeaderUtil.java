package cz.cvut.kbss.reporting.rest.handler;

public class HttpLinkHeaderUtil {

    private HttpLinkHeaderUtil() {
        throw new AssertionError();
    }

    public static String extractURIByRel(final String linkHeader, final String rel) {
        if (linkHeader == null) {
            return null;
        }
        String uriWithSpecifiedRel = null;
        final String[] links = linkHeader.split(", ");
        String linkRelation;
        for (final String link : links) {
            final int positionOfSeparator = link.indexOf(';');
            linkRelation = link.substring(positionOfSeparator + 1, link.length()).trim();
            if (extractTypeOfRelation(linkRelation).equals(rel)) {
                uriWithSpecifiedRel = link.substring(0, positionOfSeparator);
                break;
            }
        }

        return uriWithSpecifiedRel;
    }

    private static Object extractTypeOfRelation(final String linkRelation) {
        final int positionOfEquals = linkRelation.indexOf('=');
        return linkRelation.substring(positionOfEquals + 1, linkRelation.length()).trim();
    }
}
