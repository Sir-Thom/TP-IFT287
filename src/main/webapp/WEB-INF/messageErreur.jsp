<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>

<%
  // Affichage des messages d'erreur
  if (request.getAttribute("erreur") != null) {
    String erreur = (String) request.getAttribute("erreur");
%>
<div class="alert alert-danger" role="alert">
  <%= erreur %>
</div>
<%
  }

  // Ancien code pour les listes (conservé pour compatibilité)
  if (request.getAttribute("listeMessageErreur") != null) {
    List<String> listeErreurs = (List<String>) request.getAttribute("listeMessageErreur");
    for(String text : listeErreurs) {
%>
<div class="alert alert-danger" role="alert">
  <%= text %>
</div>
<%
    }
  }
%>