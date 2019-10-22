package com.arman.springsecurityrest.api.suite

import com.arman.springsecurityrest.api.unittest.user.AuthenticationTest
import com.arman.springsecurityrest.api.unittest.user.BruteForceTest
import com.arman.springsecurityrest.api.unittest.user.ChangePasswordTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
        AuthenticationTest::class,
        BruteForceTest::class,
        ChangePasswordTest::class
)
class UserSuite