# Supercollider Tonal Theory

Adds abstractions to SuperCollider based on Westgaard Tonal Theory

Link this into the user extensions folder. On linux: 	~/.local/share/SuperCollider/Extensions/

Download SC source tree to ~/supercollider (clone, can be shallow). This is just to get the testsuite folder to include it.

Create sclang_conf.yaml in ~/.local/share/SuperCollider. By default, sclang uses this
to locate include paths.

Use the example in this repo (sclang_conf.yaml) to add the include of testsuite.

# TODOs

- Octaves should start at C, not A
	- Update Interval
