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

//  def () {
//    def result = [:]
//    
//  }

}