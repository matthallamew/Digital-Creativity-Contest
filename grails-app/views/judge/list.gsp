<%@ page import="dcc.Judge" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'judge.label', default: 'Judge')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><sec:ifAnyGranted roles="ROLE_ADMIN"><a class="adminHome" href="${createLink(uri: '/admin')}">Admin Home</a></sec:ifAnyGranted></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
				<li><sec:ifNotLoggedIn><g:link controller="login">Login</g:link></sec:ifNotLoggedIn></li>
				<li><sec:ifLoggedIn><g:link controller="logout">Logout</g:link></sec:ifLoggedIn></li>
			</ul>
		</div>
		<div id="list-judge" class="content scaffold-list" role="main">
			<h1>Click on a Judge's username to edit or delete him or her.</h1>
<%--			<p><g:message code="default.list.label" args="[entityName]" /></p>--%>
			<br />
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="username" title="${message(code: 'judge.username.label', default: 'Username')}" />

						<g:sortableColumn property="firstName" title="${message(code: 'judge.firstName.label', default: 'First Name')}" />
					
						<g:sortableColumn property="lastName" title="${message(code: 'judge.lastName.label', default: 'Last Name')}" />
					
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${judgeInstanceList}" status="i" var="judgeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${judgeInstance.id}">${fieldValue(bean: judgeInstance, field: "username")}</g:link></td>
						<td>${fieldValue(bean: judgeInstance, field: "firstName")}</td>
						<td>${fieldValue(bean: judgeInstance, field: "lastName")}</td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<g:if test="${judgeInstanceTotal.toInteger() > 10}">
			<div class="pagination">
				<g:paginate total="${judgeInstanceTotal}" />
			</div>
			</g:if>
		</div>
	</body>
</html>