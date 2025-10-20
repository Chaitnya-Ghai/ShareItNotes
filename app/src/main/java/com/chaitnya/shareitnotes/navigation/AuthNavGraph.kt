package com.chaitnya.shareitnotes.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.chaitnya.auth.AuthScreen
import com.chaitnya.auth.RegisterScreen
import kotlinx.serialization.Serializable

object AuthNavGraph: BaseNavGraph {

    sealed interface Dest{

        @Serializable
        data object Root : Dest

        @Serializable
        data object Login : Dest

        @Serializable
        data object Register : Dest

        @Serializable
        data object ForgotPassword : Dest

        @Serializable
        data object ResetPassword : Dest

        @Serializable
        data object VerifyOtp : Dest

    }
    override fun build(
        modifier: Modifier,
        navController: NavController,
        navGraphBuilder: NavGraphBuilder,
    ) {
        navGraphBuilder.navigation<Dest.Root>( startDestination = Dest.Login){
            composable<Dest.Login>  {
                AuthScreen(
                    modifier,
                    onNavigateToRegister={
                        navController.navigate(Dest.Register)
                    } )
            }
            composable<Dest.Register> { RegisterScreen(
                modifier = Modifier,
                onNavigate = { navController.popBackStack() }
            ) }
            composable<Dest.ForgotPassword> {  }
            composable<Dest.ResetPassword> {  }
            composable<Dest.VerifyOtp> {  }
        }
    }
}