<%@ page import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	// affichage de la liste des messages d'erreur
	if (request.getAttribute("listeMessageErreur") != null)
	{
		for(String text : (List<String>)request.getAttribute("listeMessageErreur"))
		{
			%>
			<div class="alert alert-danger" role="alert">
			  <%= text %>
			</div>
		<%
		}
	}
%>
