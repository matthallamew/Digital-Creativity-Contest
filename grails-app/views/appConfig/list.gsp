<%@ page import="dcc.AppConfig" %>
<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'appConfig.label', default: 'Configuration Value')}" />
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
    <div id="list-appConfig" class="content scaffold-list" role="main">
      <h1><g:message code="default.list.label" args="[entityName]" /></h1>
      <br />
      <p>
        Values in this table are used throughout the application.  Certain ones, like submissionCategory, may have multiple rows in the table.  
        If you would like to add more, the Config Key must be exactly like the others.  
        So for submissionCategory, a new category can be added by adding another Config Key of submissionCategory and a Config Value of whatever you want your category to be.   
      </p>
      <p>
        Any brand new Config Key values that are added won't be used until a code change happens.
        <br/>Config Key values used currently:
        <ul>
          <li>- submissionCategory</li>
          <li>- cutOffDate</li>
        </ul>
      </p>
      <br />
      <g:if test="${flash.message}">
      <div class="message" role="status">${flash.message}</div>
      </g:if>
      <table>
        <thead>
          <tr>
          
            <g:sortableColumn property="configKey" title="${message(code: 'appConfig.configKey.label', default: 'Config Key')}" />
          
            <g:sortableColumn property="configValue" title="${message(code: 'appConfig.configValue.label', default: 'Config Value')}" />
          
            <g:sortableColumn property="remark" title="${message(code: 'appConfig.dateCreated.label', default: 'Remark')}" />
          
          </tr>
        </thead>
        <tbody>
        <g:each in="${appConfigInstanceList}" status="i" var="appConfigInstance">
          <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
          
            <td><g:link action="show" id="${appConfigInstance.id}">${fieldValue(bean: appConfigInstance, field: "configKey")}</g:link></td>
          
            <td>${fieldValue(bean: appConfigInstance, field: "configValue")}</td>
          
            <td>${fieldValue(bean: appConfigInstance, field: "remark")}</td>
          
          </tr>
        </g:each>
        </tbody>
      </table>
      <div class="pagination">
        <g:paginate total="${appConfigInstanceTotal}" />
      </div>
    </div>
  </body>
</html>