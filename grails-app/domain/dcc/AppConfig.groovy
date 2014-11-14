package dcc

class AppConfig {
	String configKey
	String configValue
	String remark = ""
	Date dateCreated
	Date lastUpdated

    static constraints = {
		configKey blank:false, index: 'configKey_idx'
		remark maxSize:1000
    }

	static mapping = {
		version false
	}
	String toString(){ configKey }
}
