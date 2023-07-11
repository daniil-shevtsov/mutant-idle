package com.daniil.shevtsov.idle.feature.tagsystem.domain

val lines = listOf(
    line(
        requiredTags = tags("mobile" to "true", "bones" to "broken"),
        entry = entry(
            "Now you can't move",
            tagChange = tags("mobile" to "false"),
            weight = 1000f,
        ),
    ),
    line(
        requiredTags = tags("mobile" to "true", "tied" to "true"),
        entry = entry(
            "Now you can't move",
            tagChange = tags("mobile" to "false"),
            weight = 1000f,
        ),
    ),
    line(
        requiredTags = tags("mobile" to "false", "tied" to "!true", "bones" to "okay"),
        entry = entry(
            "You can move again",
            tagChange = tags("mobile" to "true"),
            weight = 1000f,
        ),
    ),
    line(
        requiredTags = tags(
            "immortality" to "!true",
            "life" to "alive",
            "health" to "0"
        ),
        entry = entry(
            "You have died",
            tagChange = tags("life" to "dead"),
            weight = 1000f,
        ),
    ),
    line(
        requiredTags = tags("current action" to "stand", "mobile" to "false"),
        entry = entry(
            "You can't get up",
        )
    ),
    line(
        requiredTags = tags(
            "current action" to "stand",
            "posture" to "lying",
            "mobile" to "true",
        ),
        entry = entry(
            "You get up",
            tags("posture" to "standing")
        )
    ),
    line(
        requiredTags = tags(
            "current action" to "untie",
            "tied" to "true",
        ),
        entry = entry(
            "You free yourself",
            tags("tied" to "false")
        )
    ),
    line(
        requiredTags = tags(
            "current action" to "learn flight",
        ),
        entry = entry(
            "You've learned flight",
            tags("ability" to "flight")
        )
    ), //TODO: I can make a queue of actions
    line(
        requiredTags = tags(
            "current action" to "learn immortality",
        ),
        entry = entry(
            "You've learned immortality",
            tags("ability" to "+[immortality]")
        )
    ),
    line(
        requiredTags = tags(
            "current action" to "learn regeneration",
        ),
        entry = entry(
            "You've learned regeneration",
            tags("ability" to "+[regeneration]")
        )
    ),
    line(
        requiredTags = tags(
            "current action" to "regenerate",
            "ability" to "regeneration",
        ),
        entry = entry(
            "You regenerate to full health",
            tags(
                "bones" to "okay",
            )
        )
    ),
    line(
        requiredTags = tags("current action" to "stand", "mobile" to "true"),
        entry = entry(
            "You are now standing",
            tags("posture" to "standing")
        )
    ),
    line(
        requiredTags = tags("current action" to "stop flying"),
        entry = entry(
            "You stop flying",
            tags("posture" to "standing")
        )
    ),
    line(
        requiredTags = tags("posture" to "standing"),
        entry = entry("You stand, doing nothing")
    ),
    line(
        requiredTags = tags(
            "posture" to "lying",
            "position" to "ground",
            "current action" to "wait",
        ),
        entry = entry("You lie on the ground, doing nothing")
    ),
    line(
        requiredTags = tags("posture" to "lying", "current action" to "wait"),
        entry = entry("You lie, doing nothing")
    ),
    line(
        requiredTags = tags("position" to "low-air", "posture" to "flying", "current action" to "wait"),
        entry = entry("You fly in low-air")
    ),
    line(
        requiredTags = tags(
            "posture" to "!flying",
            "position" to "low-air",
            "indestructible" to "true"
        ),
        entry = entry(
            "You fall to the ground",
            tagChange = tags("position" to "ground", "posture" to "lying")
        ),
    ),
    line(
        requiredTags = tags("position" to "low-air", "posture" to "!flying"),
        entry = entry(
            "You fall to the ground, breaking every bone in your body",
            tagChange = tags(
                "position" to "ground",
                "posture" to "lying",
                "bones" to "broken",
                "health" to "0",
            )
        ),
    ),
    line(
        requiredTags = tags("ability" to "flight", "current action" to "fly"),
        entry = entry(
            "You start flying",
            tags("posture" to "flying", "position" to "low-air")
        )
    ),
    line(requiredTags = tags("" to ""), entry = entry("You do nothing")),
)

private fun line(
    requiredTags: SpikeTags,
    entry: LineEntry,
) = Line(requiredTags, entry)
