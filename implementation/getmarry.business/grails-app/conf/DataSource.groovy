import org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsAnnotationConfiguration

dataSource {
    pooled = false
    url = "jdbc:postgresql://localhost:5432/getmarry"
    driverClassName = "org.postgresql.Driver"
    dialect = org.hibernate.dialect.PostgreSQLDialect
    username = "postgres"
    password = "postgres"
}

hibernate {
    cache.use_second_level_cache=true
    cache.use_query_cache=true
    cache.provider_class='com.opensymphony.oscache.hibernate.OSCacheProvider'
}
// environment specific settings
environments {
	development {
		dataSource {
			//loggingSql = true
			dbCreate = "update" // one of 'create', 'create-drop','update'
			url = "jdbc:postgresql://localhost:5432/getmarry"
		}
	}
	test {
		dataSource {
			dbCreate = "update"
			url = "jdbc:postgresql://200.234.214.87:5432/apollo_ti7"
			username = "apollo_ti7"
			password = "rflqgbrkam"
		}
	}
	production {
		dataSource {
			dbCreate = "update"
			url = "jdbc:postgresql://200.234.214.87:5432/apollo_ti7"
			username = "apollo_ti7"
			password = "rflqgbrkam"
		}
	}
}