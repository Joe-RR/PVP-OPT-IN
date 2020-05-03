# PVP Opt-In

**This mod is server side _only_. Players who join the server will not need to install this to join.**  

This is a very small mod to add an opt-in for pvp on a server. By Default all player will be opted out of pvp
and unable to attack each other (players can still take fall damage and be attacked by mobs when passive/ opted out).
a player can do "/opt [playStyle]" to switch their current play style. [playStyle] can either be set to "in" or "out"
with their aliases being "passive" and "aggressive". All suggestions for the [playStyle] variable are programed in
so all the player has to do it type "/opt" and they can then tab to their desired option.
A player must be at full health in order to switch their playstyle. This is done to reduce the ways in which this system 
can be abused in a combat log type fashion.
#### Commands
/opt in - Allows pvp with other players

/opt aggressive - Allows pvp with other players

/opt out - Disallows pvp with other players

/opt passive - Disallows pvp with other players

 
## **Some questions and answers:**
 
**Can this be abused?**: I've done my best to
reduce the amount one can abuse the mod. Inorder to change PlayStyles a player must be a full health. I could add a
timer system, but I find those more annoying than anything.

**Can I change the chat colors?**: Yes but it requires you download the sourcecode and recompile it with specifc colors
you would like. If the demand is there maybe I'll add a config with some options for it.

**Why?**: For reference I run a small smp server with 10 or so friends. Five or so people are very into the pvp aspect
of the game while others want to build. It can be pretty frustrating for some when you get roped into a
pvp match and you were just trying to build your shop or something. So I created PvpOptIn to solve this issue.