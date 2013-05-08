<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'secUser.label', default: 'Admin')}" />
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
		<div id="list-secUser" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="username" title="${message(code: 'secUser.username.label', default: 'Username')}" />
					
						<g:sortableColumn property="firstName" title="${message(code: 'secUser.firstName.label', default: 'First Name')}" />
					
						<g:sortableColumn property="lastName" title="${message(code: 'secUser.lastName.label', default: 'Last Name')}" />
					
						<g:sortableColumn property="accountExpired" title="${message(code: 'secUser.accountExpired.label', default: 'Account Expired')}" />
					
						<g:sortableColumn property="accountLocked" title="${message(code: 'secUser.accountLocked.label', default: 'Account Locked')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${secUserInstanceList}" status="i" var="secUserInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${secUserInstance.id}">${fieldValue(bean: secUserInstance, field: "username")}</g:link></td>
					
						<td>${fieldValue(bean: secUserInstance, field: "firstName")}</td>
					
						<td>${fieldValue(bean: secUserInstance, field: "lastName")}</td>
					
						<td><g:formatBoolean boolean="${secUserInstance.accountExpired}" /></td>
					
						<td><g:formatBoolean boolean="${secUserInstance.accountLocked}" /></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${secUserInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
