# Supercollider Tonal Theory

Adds abstractions to SuperCollider based on Westgaard Tonal Theory

Link this into the user extensions folder. On linux: 	~/.local/share/SuperCollider/Extensions/

Add testsuite to supercollider so tests can be run

Requires quarks:
	- Quarks.install("https://github.com/smoge/Rational")

# TODOs

- refactorings
	- merge keysignature into key
	- substitute places where I pass a diatonic root TPC with a Key instance instead.
- Continue with more of porting
	- line note
	- line
	- linear operations
- declare rational number quark dependency properly
