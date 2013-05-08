<%@ page import="dcc.Judge" %>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'username', 'error')} required">
	<label for="firstName">
		<g:message code="judge.username.label" default="Username" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="username" required="" value="${judgeInstance?.username}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'firstName', 'error')} required">
	<label for="firstName">
		<g:message code="judge.firstName.label" default="First Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="firstName" required="" value="${judgeInstance?.firstName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'lastName', 'error')} required">
	<label for="lastName">
		<g:message code="judge.lastName.label" default="Last Name" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="lastName" required="" value="${judgeInstance?.lastName}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'password', 'error')} required">
	<label for="password">
		<g:message code="judge.password.label" default="Password" />
		<span class="required-indicator">*</span>
	</label>
	<g:passwordField name="password" required="" value=""/>
</div>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'enabled', 'error')}">
	<label for="password">
		<g:message code="judge.enabled.label" default="Enabled" />
	</label>
	<g:checkBox name="enabled" value="${judgeInstance?.enabled ?: true}"/>
</div>