package com.anydebloat.models

enum class DebloatMode(val displayName: String, val description: String) {
    NORMAL(
        displayName = "Normal",
        description = "Standard pm disable-user commands via Shizuku. Safe and reversible."
    ),
    ZERO_DAY(
        displayName = "ZeroDay",
        description = "Aggressive removal using hidden APIs and shell exploits. May require multiple runs."
    ),
    BRUTE(
        displayName = "Brute (Root)",
        description = "Full root access removal. Most aggressive method, deletes packages entirely."
    )
}
