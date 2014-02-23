<!doctype html>
<html>
	<head>
		<meta name="layout" content="main"/>
		<g:set var="entityName" value="${message(code: 'judge.label', default: 'Admin')}" />
		<title>Administration Page</title>
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<sec:ifNotLoggedIn><li><g:link controller="login">Login</g:link></li></sec:ifNotLoggedIn>
				<sec:ifLoggedIn><li><g:link controller="logout">Logout</g:link></li></sec:ifLoggedIn>
				</li>
			</ul>
		</div>

	  <div class="content">
	  	<h1>Administration Page</h1>
	  	<br />
		<g:if test="${flash.message}">
		<div class="errors" role="errors">${flash.message}</div>
		</g:if>	  	
	  	<p>${submissionsText} in the database</p>
	  	<br /><br />
  		<table>
  			<tbody>
   				<tr>
  					<td><input class="button-normal" type="button" onclick="location.href='/dcc/admin/showScoring';" value="Show Scoring" /></td>
  					<td><input class="button-normal" type="button" onclick="location.href='/dcc/admin/list';" value="Show Curent Admins" /></td>
  				</tr>
  				<tr>
  					<td><input class="button-normal" type="button" onclick="location.href='/dcc/submission/list';" value="Show Submissions" /></td>
  					<td><input class="button-normal" type="button" onclick="location.href='/dcc/judge/list';" value="Show Curent Judges" /></td>
  				</tr>
  				<tr>
  					<td><input class="button-normal" type="button" onclick="location.href='/dcc/submissionArchive';" value="Submissions Archive" /></td>
  					<td><input class="button-warn" type="button" onclick="location.href='/dcc/admin/updateGrandTotal';" value="Update Grand Total" /></td>
  				</tr>
  			</tbody>
  		</table>
	  </div>
	</body>
</html>