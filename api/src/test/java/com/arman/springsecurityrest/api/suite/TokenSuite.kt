package com.arman.springsecurityrest.api.suite

import com.arman.springsecurityrest.api.unittest.token.TokenTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        TokenTest::class
)
class TokenSuite