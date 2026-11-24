package com.example.yamakoz.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.yamakoz.data.repository.ShopRepository
import com.example.yamakoz.ui.auth.*
import com.example.yamakoz.ui.cart.*
import com.example.yamakoz.ui.catalog.*
import com.example.yamakoz.ui.cheque.*
import com.example.yamakoz.ui.order.*
import kotlinx.coroutines.launch

private const val CATALOG = "catalog"
private const val PRODUCTS = "products/{id}"
private const val CART = "cart"
private const val SEARCH = "search"
private const val ORDER = "order/{id}"
private const val CHEQUE = "cheque/{id}"
private val idArgument = listOf(navArgument("id") { type = NavType.LongType })

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopNavHost(repository: ShopRepository) {
    val nav = rememberNavController()
    var customerId by rememberSaveable { mutableStateOf<Long?>(null) }
    var phone by rememberSaveable { mutableStateOf("") }
    val owner = customerId
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val message: (String) -> Unit = { text ->
        scope.launch {
            snackbar.currentSnackbarData?.dismiss()
            snackbar.showSnackbar(text)
        }
    }
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route
    val title = when (route) {
        CATALOG -> "Каталог"
        PRODUCTS -> "Товары"
        CART -> "Корзина"
        SEARCH -> "Поиск заказа"
        ORDER -> "Детали заказа"
        CHEQUE -> "Электронный чек"
        else -> "Магазин бытовой техники"
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbar) },
        topBar = { TopAppBar(title = { Text(title) }) },
        bottomBar = {
            if (owner != null) {
                val currentTab = when (route) {
                    CART -> CART
                    SEARCH, ORDER, CHEQUE -> SEARCH
                    else -> CATALOG
                }
                ShopBottomBar(currentTab) { destination ->
                    if (route != destination) {
                        if (destination == CATALOG) {
                            nav.popBackStack(CATALOG, inclusive = false)
                        } else {
                            nav.navigate(destination) {
                                popUpTo(CATALOG)
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
        },
    ) { padding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .imePadding()
        if (owner == null) {
            Box(modifier) {
                val model = viewModel<AuthViewModel>(factory = viewModelFactory {
                    initializer { AuthViewModel(repository) }
                })
                AuthRoute(model, message) { customer ->
                    phone = customer.phone
                    customerId = customer.id
                }
            }
        } else {
            NavHost(nav, startDestination = CATALOG, modifier = modifier) {
                composable(CATALOG) {
                    val model = viewModel<CatalogViewModel>(factory = viewModelFactory {
                        initializer { CatalogViewModel(repository) }
                    })
                    CatalogRoute(model) { nav.navigate("products/$it") { launchSingleTop = true } }
                }
                composable(PRODUCTS, arguments = idArgument) { page ->
                    val id = requireNotNull(page.arguments).getLong("id")
                    val model = viewModel<ProductsViewModel>(factory = viewModelFactory {
                        initializer {
                            ProductsViewModel(
                                repository,
                                createSavedStateHandle(),
                                owner,
                                id
                            )
                        }
                    })
                    ProductsRoute(model, message)
                }
                composable(CART) {
                    val model = viewModel<CartViewModel>(factory = viewModelFactory {
                        initializer { CartViewModel(repository, owner) }
                    })
                    CartRoute(model, message) { id ->
                        nav.navigate("order/$id") { popUpTo(CART) { inclusive = true } }
                    }
                }
                composable(SEARCH) {
                    val model = viewModel<OrderSearchViewModel>(factory = viewModelFactory {
                        initializer { OrderSearchViewModel(repository, owner) }
                    })
                    OrderSearchRoute(model, message) {
                        nav.navigate("order/$it") {
                            launchSingleTop = true
                        }
                    }
                }
                composable(ORDER, arguments = idArgument) { page ->
                    val id = requireNotNull(page.arguments).getLong("id")
                    val model = viewModel<OrderViewModel>(factory = viewModelFactory {
                        initializer { OrderViewModel(repository, owner, id) }
                    })
                    OrderRoute(
                        model,
                        phone,
                        message
                    ) { nav.navigate("cheque/$id") { launchSingleTop = true } }
                }
                composable(CHEQUE, arguments = idArgument) { page ->
                    val id = requireNotNull(page.arguments).getLong("id")
                    val model = viewModel<ChequeViewModel>(factory = viewModelFactory {
                        initializer { ChequeViewModel(repository, owner, id) }
                    })
                    ChequeRoute(model)
                }
            }
        }
    }
}

@Composable
private fun ShopBottomBar(selected: String, navigate: (String) -> Unit) {
    val tabs = listOf(
        Triple(CATALOG, "Каталог", ShopTabIcon.Catalog),
        Triple(CART, "Корзина", ShopTabIcon.Cart),
        Triple(SEARCH, "Заказы", ShopTabIcon.Orders),
    )
    Surface(color = MaterialTheme.colorScheme.surfaceContainer, tonalElevation = 3.dp) {
        Row(
            Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            for ((destination, label, icon) in tabs) {
                val active = selected == destination
                Box(
                    Modifier
                        .weight(1f)
                        .heightIn(min = 56.dp)
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(if (active) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                        .selectable(
                            selected = active,
                            role = Role.Tab,
                            onClick = { navigate(destination) }),
                    contentAlignment = Alignment.Center,
                ) {
                    val tint = if (active) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurfaceVariant
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp),
                    ) {
                        ShopNavigationIcon(icon, tint)
                        Text(label, color = tint, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}
