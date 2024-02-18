package com.airbnb.android.showkase.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.airbnb.android.showkase.models.ShowkaseBrowserScreenMetadata
import com.airbnb.android.showkase.models.ShowkaseCategory
import com.airbnb.android.showkase.models.ShowkaseCurrentScreen
import com.airbnb.android.showkase.models.clear
import com.airbnb.android.showkase.models.clearActiveSearch
import com.airbnb.android.showkase.models.update
import java.util.Locale

// MutableState 作为参数类型这种写法不应该
@Composable
internal fun ShowkaseCategoriesScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController,
    categoryMetadataMap: Map<ShowkaseCategory, Int>
) {
    // 强转为 AppCompatActivity 也不应该，最多是 Actvity
    val activity = LocalContext.current as AppCompatActivity
    LazyColumn {
        items(
            items = categoryMetadataMap.entries.toList(),
            itemContent = { (category, categorySize) ->
                val defaultLocale = Locale.getDefault()
                val title = category.name
                    .lowercase(defaultLocale)
                    .replaceFirstChar { it.titlecase(defaultLocale) }

                // 最外层的仅展示 Group 名称
                SimpleTextCard(
                    text = "$title ($categorySize)",
                    onClick = {
                        showkaseBrowserScreenMetadata.update {
                            copy(
                                currentGroup = null,
                                isSearchActive = false,
                                searchQuery = null
                            )
                        }
                        when (category) {
                            ShowkaseCategory.COMPONENTS -> navController.navigate(
                                ShowkaseCurrentScreen.COMPONENT_GROUPS
                            )
                            ShowkaseCategory.COLORS -> navController.navigate(
                                ShowkaseCurrentScreen.COLOR_GROUPS
                            )
                            ShowkaseCategory.TYPOGRAPHY -> navController.navigate(
                                ShowkaseCurrentScreen.TYPOGRAPHY_GROUPS
                            )
                        }
                    }
                )
            }
        )
    }
    BackButtonHandler {
        goBackFromCategoriesScreen(activity, showkaseBrowserScreenMetadata)
    }
}

// 这里一定要使用 activity.finish 吗，不能一直向上传递进而 pop 掉所有页面？
private fun goBackFromCategoriesScreen(
    activity: AppCompatActivity,
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>
) {
    val isSearchActive = showkaseBrowserScreenMetadata.value.isSearchActive
    when {
        isSearchActive -> showkaseBrowserScreenMetadata.clearActiveSearch()
        else -> activity.finish()
    }
}

internal fun goBackToCategoriesScreen(
    showkaseBrowserScreenMetadata: MutableState<ShowkaseBrowserScreenMetadata>,
    navController: NavHostController,
    onBackPressOnRoot: () -> Unit
) {
    when {
        showkaseBrowserScreenMetadata.value.isSearchActive -> {
            showkaseBrowserScreenMetadata.clearActiveSearch()
        }
        navController.currentDestination?.id == navController.graph.startDestinationId -> {
            onBackPressOnRoot()
        }
        else -> {
            showkaseBrowserScreenMetadata.clear()
            navController.navigate(ShowkaseCurrentScreen.SHOWKASE_CATEGORIES)
        }
    }
}
