<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'submission.label', default: 'Submission')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><sec:ifAnyGranted roles="ROLE_ADMIN"><a class="adminHome" href="${createLink(uri: '/admin')}">Admin Home</a></sec:ifAnyGranted></li>
				<li><sec:ifNotLoggedIn><g:link controller="login">Login</g:link></sec:ifNotLoggedIn></li>
				<li><sec:ifLoggedIn><g:link controller="logout">Logout</g:link></sec:ifLoggedIn></li>
			</ul>
		</div>
		<div id="list-submission" class="content">
			<h3>Choose a category to see the ranks</h3>
			<table>
				<tbody>
				<g:each in="${categoryList}" status="x" var="category">
					<tr>
						<td><g:link action='showScoring' params='[category: "${category}"]'><strong>${category}</strong></g:link></td>
					</tr>
				</g:each>
				</tbody>
			</table>
			<g:if test="${haveCategory}">
				<h3>${category}</h3>
				<table>
					<tbody>
						<g:each in="${submissionList}" status="y" var="submission">
							<tr>
								<td>
									<h4><g:link action="showSubmission" id="${submission.id}">${submission.title}</g:link></h4>
									By: ${submission.author}
								</td>
							</tr>
							<tr>
								<td>
									Total Points:${submission.grandTotal}
								</td>
							</tr>
						</g:each>
					</tbody>
				</table>
		        <div class="paginateButtons">
		            <g:paginate total="${submissionListTotal}"
		            params="[category:params.category,haveCategory:haveCategory]"/>
		        </div>
			</g:if>
		</div>
	</body>	
</html>