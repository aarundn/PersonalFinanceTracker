package com.example.core.navigation.features

import com.example.core.navigation.AppRoutes
import com.example.core.navigation.Feature

interface HomeFeature: Feature {
    fun homeRoute(): AppRoutes
}