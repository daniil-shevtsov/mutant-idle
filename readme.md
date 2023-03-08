## Inspirations
![inspirations](images/Pasted image 20230308121629.png)
Setting is inspired by various movies, tv shows and games about monsters and other non-humans.

Main inspiration for gameplay are Evolve Idle. In this game you evolve from simple life forms to intergalactic travel. It has great replayability and variety. You can evolve into various creatures from humans, orcs and elves to fungi, ants, demons and underwater civilizations. 
Then there is variety in civilization stages, they also can be different. The basic gameplay stays the same but it feels different because of the text and modificators.
And what you do in the game unlocks new races and civilizations. For example, if you bury deep into the mars and open the portal to hell apart from new mechanics of fighting the demons it unlocks demons as playable race.
But, in my opinion, the game does not utilise these features to its full potential.
I wanted to find a cheap way to implement as much variety as I can, so the game would feel different when you are playing as different creatures.

## Gameplay Loop
![[Pasted image 20230308122420.png]]
The main idea of the gameplay is the following:
* Select a monster species and a job
* Then through actions and upgrades you need to balance gaining power and increasing suspicion.
* The game ends when either you are found out and eliminated or you reach your goal
* You keep unlocking new species, powers, jobs, goals etc. This adds extreme variety, you always are guessing what interesting thing is next
## Features
### Tag System
![[Pasted image 20221212103708.png]]
![[Pasted image 20221212105235.png]]
![[Pasted image 20221212105342.png]]
My main goal was to make variety cost as low as possible. I've read twitter thread by the developer of Weird West about their tag system. That sounded great so I've implemented a simplified version of that.
Tags can be added, removed or replaced by anything: monster species, job, current location, actions or upgrades.
Anything can require some tag. For example, Graveyard location is accessible only if you have a job with Graveyard Access tag.
Here are some examples of that. Vampire has tags of Vampire species, fear of sun, magic, immortality and hypnosis.
### Species
![[Pasted image 20221211175808.png]]
Selection of the species and job. Right now the following species implemented and some testing jobs. But this is just to show the implemented system, new species and jobs are extremely easy to add.
### Upgrades
![[Pasted image 20221212120008.png]]

![[Pasted image 20221222102124.png]]
You can purchase upgrades which add some tags. For example, devourer can develop super strength which allows to capture people and strong jaws that allow to eat the remains in order to lower suspicion.
### Actions
![[Pasted image 20221212105456.png]]

![[Pasted image 20221212105643.png]]
Actions can require and spend resources. For example eating a rat gives you fresh meat resource.
### Flavors
![[Pasted image 20230308123852.png]]
I've found various cheap ways to increase variety. Species have different color palletes, right now they are just for testing as a proof of concept. Also I've implemented what I call flavors system. Every text in the projecct allows for adding placeholders that can be replaced with different things depending on tags. 
The same action of invisibility described differently when you are a magical creature (like vampire or demon) and when you are technical (like alien or android).
Another example is that people are refered by different names depending on your creature. Devourer looks at people as food and shape shifter looks at them as clothes.
Such things are easy to add but they make the playthroughs feel more fresh.
