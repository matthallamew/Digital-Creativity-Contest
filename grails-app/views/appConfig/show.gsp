
<%@ page import="dcc.AppConfig" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'appConfig.label', default: 'AppConfig')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-appConfig" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-appConfig" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list appConfig">
			
				<g:if test="${appConfigInstance?.configKey}">
				<li class="fieldcontain">
					<span id="configKey-label" class="property-label"><g:message code="appConfig.configKey.label" default="Config Key" /></span>
					
						<span class="property-value" aria-labelledby="configKey-label"><g:fieldValue bean="${appConfigInstance}" field="configKey"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${appConfigInstance?.remark}">
				<li class="fieldcontain">
					<span id="remark-label" class="property-label"><g:message code="appConfig.remark.label" default="Remark" /></span>
					
						<span class="property-value" aria-labelledby="remark-label"><g:fieldValue bean="${appConfigInstance}" field="remark"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${appConfigInstance?.configValue}">
				<li class="fieldcontain">
					<span id="configValue-label" class="property-label"><g:message code="appConfig.configValue.label" default="Config Value" /></span>
					
						<span class="property-value" aria-labelledby="configValue-label"><g:fieldValue bean="${appConfigInstance}" field="configValue"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${appConfigInstance?.dateCreated}">
				<li class="fieldcontain">
					<span id="dateCreated-label" class="property-label"><g:message code="appConfig.dateCreated.label" default="Date Created" /></span>
					
						<span class="property-value" aria-labelledby="dateCreated-label"><g:formatDate date="${appConfigInstance?.dateCreated}" /></span>
					
				</li>
				</g:if>
			
				<g:if test="${appConfigInstance?.lastUpdated}">
				<li class="fieldcontain">
					<span id="lastUpdated-label" class="property-label"><g:message code="appConfig.lastUpdated.label" default="Last Updated" /></span>
					
						<span class="property-value" aria-labelledby="lastUpdated-label"><g:formatDate date="${appConfigInstance?.lastUpdated}" /></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${appConfigInstance?.id}" />
					<g:link class="edit" action="edit" id="${appConfigInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
