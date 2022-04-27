package com.daniil.shevtsov.idle.feature.upgrade.domain

fun upgrade(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    price: Double = 0.0,
    status: UpgradeStatus = UpgradeStatus.NotBought,
) = Upgrade(
    id = id,
    title = title,
    subtitle = subtitle,
    price = price(value = price),
    status = status,
)

fun price(
    value: Double = 0.0,
) = Price(
    value = value,
)
