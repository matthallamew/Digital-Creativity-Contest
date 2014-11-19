<%@ page import="dcc.AppConfig" %>
<div class="fieldcontain ${hasErrors(bean: appConfigInstance, field: 'configKey', 'error')} required">
  <label for="configKey">
    <g:message code="appConfig.configKey.label" default="Config Key" />
    <span class="required-indicator">*</span>
  </label>
  <g:textField name="configKey" required="" value="${appConfigInstance?.configKey}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: appConfigInstance, field: 'configValue', 'error')}">
  <label for="configValue">
    <g:message code="appConfig.configValue.label" default="Config Value" />
<%--    <span class="required-indicator">*</span>--%>
  </label>
  <g:textField name="configValue" value="${appConfigInstance?.configValue}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: appConfigInstance, field: 'remark', 'error')}">
  <label for="remark">
    <g:message code="appConfig.remark.label" default="Remark" />
  </label>
  <g:textArea name="remark" value="${appConfigInstance?.remark}"/>
</div>