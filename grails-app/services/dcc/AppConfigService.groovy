package dcc

import org.springframework.transaction.annotation.Transactional

class AppConfigService {

  def list(Integer max, params) {
    def result = [:]
    params.max = Math.min(max ?: 10,100)
    result.appConfigInstanceList = AppConfig.list(params)
    result.appConfigInstanceTotal = AppConfig.count()
    if(!result){
      result.error = [code:"default.method.failure",args:["Could not produce a list of Configuration Values",""]]
    }
    return result
  }

  @Transactional
  def create(params) {
    def result = [:]
    result.appConfigInstance = new AppConfig(params)
    if(!result.appConfigInstance){
      result.error = [code:"default.method.failure",args:["Could not create a new Configuration Value.",""]]
    }
    return result
    
  }

  @Transactional
  def save (params) {
    def result = [:]
    result.appConfigInstance = new AppConfig(params)
    if(result.appConfigInstance.hasErrors() || !result.appConfigInstance.save(flush:true)){
      result.error = [code:"default.method.failure",args: ["Could not create new Configuration Value",""]]
    }
    return result
  }

  def show(Long id) {
    def result = [:]
    result.appConfigInstance = AppConfig.read(id)
    if(!result.appConfigInstance){
      result.error = [code:"default.not.found.message",args:['Configuration Value',id]]
    }
    return result
  }

  @Transactional
  def edit (Long id) {
    def result = [:]
    result.appConfigInstance = AppConfig.get(id)
    if(!result.appConfigInstance){
      result.error = [code:"default.not.found.message",args:["Configuration Value",id]]
    }
    return result
  }

  @Transactional
  def update(Long id,params) {
    def result = [:]
    result.appConfigInstance = AppConfig.get(id)
    // not found
    if(!result.appConfigInstance){
      result.error = [code: "default.not.found.message",args:["Configuration Value",id]]
      return result
    }

    result.appConfigInstance.properties = params

    // could not save
    if(result.appConfigInstance.hasErrors() || !result.appConfigInstance.save(flush:true)){
      result.error = [code:"default.method.failure",args:["Could not update Configuration Value",""]]
      return result
    }
    //success
    return result
  }

  @Transactional
  def delete(Long id) {
    def result = [:]
    result.appConfigInstance = AppConfig.get(id)
    if(!result.appConfigInstance){
      result.error = [code:"default.not.found.message",args:["Configuration Value",id]]
      return result
    }
    result.appConfigInstance.delete(flush:true)
    return result
  }

}