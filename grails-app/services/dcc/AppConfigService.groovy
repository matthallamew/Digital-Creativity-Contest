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

//  def () {
//    def result = [:]
//    
//  }

}