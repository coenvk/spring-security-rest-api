package com.arman.springsecurityrest.api

import com.arman.springsecurityrest.api.suite.TokenSuite
import com.arman.springsecurityrest.api.suite.UserSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        UserSuite::class,
        TokenSuite::class
)
class ApiSuite