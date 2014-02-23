<%@ page import="dcc.Rank" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'rank.label', default: 'Rank')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-rank" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="skill" title="${message(code: 'rank.skill.label', default: 'Skill')}" />
					
						<g:sortableColumn property="skillComment" title="${message(code: 'rank.skillComment.label', default: 'Skill Comment')}" />
					
						<g:sortableColumn property="creativity" title="${message(code: 'rank.creativity.label', default: 'Creativity')}" />
					
						<g:sortableColumn property="creativityComment" title="${message(code: 'rank.creativityComment.label', default: 'Creativity Comment')}" />
					
						<g:sortableColumn property="aesthetic" title="${message(code: 'rank.aesthetic.label', default: 'Aesthetic')}" />
					
						<g:sortableColumn property="aestheticComment" title="${message(code: 'rank.aestheticComment.label', default: 'Aesthetic Comment')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${rankInstanceList}" status="i" var="rankInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${rankInstance.id}">${fieldValue(bean: rankInstance, field: "skill")}</g:link></td>
					
						<td>${fieldValue(bean: rankInstance, field: "skillComment")}</td>
					
						<td>${fieldValue(bean: rankInstance, field: "creativity")}</td>
					
						<td>${fieldValue(bean: rankInstance, field: "creativityComment")}</td>
					
						<td>${fieldValue(bean: rankInstance, field: "aesthetic")}</td>
					
						<td>${fieldValue(bean: rankInstance, field: "aestheticComment")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${rankInstanceTotal}" />
			</div>
		</div>
	</body>
</html>