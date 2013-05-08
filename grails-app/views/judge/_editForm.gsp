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

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'enabled', 'error')}">
	<label for="enabled">
		<g:message code="judge.enabled.label" default="Enabled" />
	</label>
	<g:checkBox name="enabled" value="${judgeInstance?.enabled ?: true}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'accountExpired', 'error')}">
	<label for="accountExpired">
		<g:message code="judge.accountExpired.label" default="Account Expired" />
	</label>
	<g:checkBox name="accountExpired" value="${judgeInstance?.accountExpired ?: false}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'accountLocked', 'error')}">
	<label for="accountLocked">
		<g:message code="judge.accountLocked.label" default="Account Locked" />
	</label>
	<g:checkBox name="accountLocked" value="${judgeInstance?.accountLocked ?: false}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: judgeInstance, field: 'passwordExpired', 'error')}">
	<label for="passwordExpired">
		<g:message code="judge.passwordExpired.label" default="Password Expired" />
	</label>
	<g:checkBox name="passwordExpired" value="${judgeInstance?.passwordExpired ?: false}"/>
</div>
