package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.entry
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.line
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tags

val plotLines = listOf(
    line(
        requiredTags = tags("mobile" to "true", "bones" to "broken"),
        entry = entry(
            "Now you can't move",
            tagChanges = tags("mobile" to "false"),
            weight = 1000f,
        ),
    ),
    line(
        requiredTags = tags("mobile" to "true", "tied" to "true"),
        entry = entry(
            "Now you can't move",
            tagChanges = tags("mobile" to "false"),
            weight = 1000f,
        ),
    ),
    line(
        requiredTags = tags("mobile" to "false", "tied" to "!true", "bones" to "okay"),
        entry = entry(
            "You can move again",
            tagChanges = tags("mobile" to "true"),
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
            tagChanges = tags("life" to "dead"),
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
            tagChanges = tags("posture" to "standing")
        )
    ),
    line(
        requiredTags = tags(
            "current action" to "untie",
            "tied" to "true",
        ),
        entry = entry(
            "You free yourself",
            tagChanges = tags("tied" to "false")
        )
    ),
    line(
        requiredTags = tags(
            "current action" to "learn flight",
        ),
        entry = entry(
            "You've learned flight",
            tagChanges = tags("ability" to "flight")
        )
    ), //TODO: I can make a queue of actions
    line(
        requiredTags = tags(
            "current action" to "learn immortality",
        ),
        entry = entry(
            "You've learned immortality",
            tagChanges = tags("ability" to "+[immortality]")
        )
    ),
    line(
        requiredTags = tags(
            "current action" to "learn regeneration",
        ),
        entry = entry(
            "You've learned regeneration",
            tagChanges = tags("ability" to "+[regeneration]")
        )
    ),
    line(
        requiredTags = tags(
            "current action" to "regenerate",
            "ability" to "regeneration",
        ),
        entry = entry(
            "You regenerate to full health",
            tagChanges = tags(
                "bones" to "okay",
            )
        )
    ),
    line(
        requiredTags = tags("current action" to "stand", "mobile" to "true"),
        entry = entry(
            "You are now standing",
            tagChanges = tags("posture" to "standing")
        )
    ),
    line(
        requiredTags = tags("current action" to "stop flying"),
        entry = entry(
            "You stop flying",
            tagChanges = tags("posture" to "standing")
        )
    ),
    line(
        requiredTags = tags("posture" to "standing", "current action" to "wait"),
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
        requiredTags = tags(
            "position" to "low-air",
            "posture" to "flying",
            "current action" to "wait"
        ),
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
            tagChanges = tags("position" to "ground", "posture" to "lying")
        ),
    ),
    line(
        requiredTags = tags("position" to "low-air", "posture" to "!flying"),
        entry = entry(
            "You fall to the ground, breaking every bone in your body",
            tagChanges = tags(
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
            tagChanges = tags("posture" to "flying", "position" to "low-air")
        )
    ),
    /**
     * TODO: Holding : knife and knife has its own tags somehow. Like edged weapon, etc.
     * Maybe it should be outside of tag system itself, like everything is jjust added to the tags pile
     */
    line(
        requiredTags = tags("sharp weapon" to "true", "current action" to "cut your hand"),
        entry = entry(
            "You cut your hand",
            tagChanges = tags("bleeding" to "true", "health" to "\${-15}")
        )
    ),
    line(
        requiredTags = tags("objects" to "knife", "current action" to "pick up knife"),
        entry = entry(
            "You pick up the knife",
            tagChanges = tags(
                "holding" to "knife",
                "sharp weapon" to "true",
                "short weapon" to "true",
                "throwable weapon" to "true"
            )
        )
    ),
    line(requiredTags = tags("" to ""), entry = entry("You do nothing")),
)
