
<%@ page import="dcc.Rank" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'rank.label', default: 'Rank')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-rank" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><sec:ifAnyGranted roles="ROLE_ADMIN"><a class="adminHome" href="${createLink(uri: '/admin')}">Admin Home</a></sec:ifAnyGranted></li>
				<li><sec:ifAnyGranted roles="ROLE_ADMIN"><g:link controller="admin" action="showScoring">Show Scoring</g:link></sec:ifAnyGranted></li>
				<li><a href="javascript:history.go(-1);">Back</a></li>

<%--				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>--%>
<%--				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>--%>
			</ul>
		</div>
		<div id="show-rank" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list rank">
			
				<g:if test="${rankInstance?.skill}">
				<li class="fieldcontain">
					<span id="skill-label" class="property-label"><g:message code="rank.skill.label" default="Skill" /></span>
					
						<span class="property-value" aria-labelledby="skill-label"><g:fieldValue bean="${rankInstance}" field="skill"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.skillComment}">
				<li class="fieldcontain">
					<span id="skillComment-label" class="property-label"><g:message code="rank.skillComment.label" default="Skill Comment" /></span>
					
						<span class="property-value" aria-labelledby="skillComment-label"><g:fieldValue bean="${rankInstance}" field="skillComment"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.creativity}">
				<li class="fieldcontain">
					<span id="creativity-label" class="property-label"><g:message code="rank.creativity.label" default="Creativity" /></span>
					
						<span class="property-value" aria-labelledby="creativity-label"><g:fieldValue bean="${rankInstance}" field="creativity"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.creativityComment}">
				<li class="fieldcontain">
					<span id="creativityComment-label" class="property-label"><g:message code="rank.creativityComment.label" default="Creativity Comment" /></span>
					
						<span class="property-value" aria-labelledby="creativityComment-label"><g:fieldValue bean="${rankInstance}" field="creativityComment"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.aesthetic}">
				<li class="fieldcontain">
					<span id="aesthetic-label" class="property-label"><g:message code="rank.aesthetic.label" default="Aesthetic" /></span>
					
						<span class="property-value" aria-labelledby="aesthetic-label"><g:fieldValue bean="${rankInstance}" field="aesthetic"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.aestheticComment}">
				<li class="fieldcontain">
					<span id="aestheticComment-label" class="property-label"><g:message code="rank.aestheticComment.label" default="Aesthetic Comment" /></span>
					
						<span class="property-value" aria-labelledby="aestheticComment-label"><g:fieldValue bean="${rankInstance}" field="aestheticComment"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.purpose}">
				<li class="fieldcontain">
					<span id="purpose-label" class="property-label"><g:message code="rank.purpose.label" default="Purpose" /></span>
					
						<span class="property-value" aria-labelledby="purpose-label"><g:fieldValue bean="${rankInstance}" field="purpose"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.purposeComment}">
				<li class="fieldcontain">
					<span id="purposeComment-label" class="property-label"><g:message code="rank.purposeComment.label" default="Purpose Comment" /></span>
					
						<span class="property-value" aria-labelledby="purposeComment-label"><g:fieldValue bean="${rankInstance}" field="purposeComment"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.total}">
				<li class="fieldcontain">
					<span id="total-label" class="property-label"><g:message code="rank.total.label" default="Total" /></span>
					
						<span class="property-value" aria-labelledby="total-label"><g:fieldValue bean="${rankInstance}" field="total"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.submissions}">
				<li class="fieldcontain">
					<span id="submissions-label" class="property-label"><g:message code="rank.submissions.label" default="Submission" /></span>
					
						<span class="property-value" aria-labelledby="submission-label"><g:link controller="admin" action="showSubmission" id="${rankInstance?.submissions?.id}">${rankInstance?.submissions?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
				<g:if test="${rankInstance?.judges}">
				<li class="fieldcontain">
					<span id="judges-label" class="property-label"><g:message code="rank.judges.label" default="Judges" /></span>
					
						<span class="property-value" aria-labelledby="judges-label"><g:link controller="judge" action="show" id="${rankInstance?.judges?.id}">${rankInstance?.judges?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
<%--			<g:form>--%>
<%--				<fieldset class="buttons">--%>
<%--					<g:hiddenField name="id" value="${rankInstance?.id}" />--%>
<%--					<g:link class="edit" action="edit" id="${rankInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>--%>
<%--					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />--%>
<%--				</fieldset>--%>
<%--			</g:form>--%>
		</div>
	</body>
</html>
