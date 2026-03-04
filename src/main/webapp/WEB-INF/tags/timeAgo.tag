<%@ tag body-content="empty" %>
<%@ attribute name="date" required="true" type="java.time.LocalDateTime" %>
<%@ tag import="java.time.LocalDateTime" %>
<%@ tag import="java.time.Duration" %>
<%@ tag import="java.time.ZoneOffset" %>
<%@ tag import="java.time.format.DateTimeFormatter" %>

<%
    if (date == null) {
        out.print("-");
        return;
    }

    LocalDateTime now = LocalDateTime.now();

    Duration duration = Duration.between(date, now);
    long seconds = duration.getSeconds();

    if (seconds < 0) {
        if (seconds > -300) {
            out.print("Just now");
            return;
        }
        else {
            out.print(date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
            return;
        }
    }
    if (seconds < 60) {
        out.print("Just now");
    } else if (seconds < 3600) {
        long mins = seconds / 60;
        out.print(mins + (mins == 1 ? " min ago" : " mins ago"));
    } else if (seconds < 86400) {
        long hours = seconds / 3600;
        out.print(hours + (hours == 1 ? " hour ago" : " hours ago"));
    } else if (seconds < 604800) {
        long days = seconds / 86400;
        out.print(days + (days == 1 ? " day ago" : " days ago"));
    } else {
        out.print(date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
    }
%>