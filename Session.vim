let SessionLoad = 1
let s:so_save = &g:so | let s:siso_save = &g:siso | setg so=0 siso=0 | setl so=-1 siso=-1
let v:this_session=expand("<sfile>:p")
silent only
silent tabonly
cd ~/TonalTheory
if expand('%') == '' && !&modified && line('$') <= 1 && getline(1) == ''
  let s:wipebuf = bufnr('%')
endif
let s:shortmess_save = &shortmess
if &shortmess =~ 'A'
  set shortmess=aoOA
else
  set shortmess=aoO
endif
badd +21 README.md
badd +10 ~/TonalTheory/scratch.scd
badd +111 classes/TestIntervalSymbol.sc
badd +312 ~/TonalTheory/classes/IntervalSymbol.sc
badd +754 \[scnvim]
badd +106 ~/TonalTheory/classes/TTLine.sc
badd +1 ~/TonalTheory/classes/TestTTLine.sc
badd +13 ~/TonalTheory/classes/KeySignature.sc
badd +14 ~/TonalTheory/classes/Counterpoint.sc
badd +1 ~/TonalTheory/classes/TestCounterpoint.sc
badd +28 ~/TonalTheory/classes/DiatonicCollection.sc
badd +1 ~/TonalTheory/classes/NoteSymbol.sc
badd +152 classes/TonalPitchClassSymbol.sc
badd +35 classes/TestTonalPitchClassSymbol.sc
badd +53 ~/TonalTheory/classes/TestNoteSymbol.sc
argglobal
%argdel
$argadd README.md
set stal=2
tabnew +setlocal\ bufhidden=wipe
tabnew +setlocal\ bufhidden=wipe
tabrewind
edit README.md
argglobal
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 19 - ((18 * winheight(0) + 23) / 46)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 19
normal! 031|
tabnext
edit ~/TonalTheory/classes/Counterpoint.sc
let s:save_splitbelow = &splitbelow
let s:save_splitright = &splitright
set splitbelow splitright
wincmd _ | wincmd |
vsplit
1wincmd h
wincmd _ | wincmd |
split
1wincmd k
wincmd w
wincmd w
let &splitbelow = s:save_splitbelow
let &splitright = s:save_splitright
wincmd t
let s:save_winminheight = &winminheight
let s:save_winminwidth = &winminwidth
set winminheight=0
set winheight=1
set winminwidth=0
set winwidth=1
exe '1resize ' . ((&lines * 22 + 24) / 49)
exe 'vert 1resize ' . ((&columns * 97 + 97) / 194)
exe '2resize ' . ((&lines * 23 + 24) / 49)
exe 'vert 2resize ' . ((&columns * 97 + 97) / 194)
exe 'vert 3resize ' . ((&columns * 96 + 97) / 194)
argglobal
balt classes/TonalPitchClassSymbol.sc
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 18 - ((11 * winheight(0) + 11) / 22)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 18
normal! 027|
wincmd w
argglobal
if bufexists(fnamemodify("~/TonalTheory/classes/NoteSymbol.sc", ":p")) | buffer ~/TonalTheory/classes/NoteSymbol.sc | else | edit ~/TonalTheory/classes/NoteSymbol.sc | endif
if &buftype ==# 'terminal'
  silent file ~/TonalTheory/classes/NoteSymbol.sc
endif
balt classes/TonalPitchClassSymbol.sc
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 80 - ((10 * winheight(0) + 11) / 23)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 80
normal! 052|
wincmd w
argglobal
if bufexists(fnamemodify("~/TonalTheory/classes/TestNoteSymbol.sc", ":p")) | buffer ~/TonalTheory/classes/TestNoteSymbol.sc | else | edit ~/TonalTheory/classes/TestNoteSymbol.sc | endif
if &buftype ==# 'terminal'
  silent file ~/TonalTheory/classes/TestNoteSymbol.sc
endif
balt ~/TonalTheory/classes/TestCounterpoint.sc
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 57 - ((33 * winheight(0) + 23) / 46)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 57
normal! 05|
wincmd w
2wincmd w
exe '1resize ' . ((&lines * 22 + 24) / 49)
exe 'vert 1resize ' . ((&columns * 97 + 97) / 194)
exe '2resize ' . ((&lines * 23 + 24) / 49)
exe 'vert 2resize ' . ((&columns * 97 + 97) / 194)
exe 'vert 3resize ' . ((&columns * 96 + 97) / 194)
tabnext
edit ~/TonalTheory/scratch.scd
let s:save_splitbelow = &splitbelow
let s:save_splitright = &splitright
set splitbelow splitright
wincmd _ | wincmd |
vsplit
1wincmd h
wincmd w
let &splitbelow = s:save_splitbelow
let &splitright = s:save_splitright
wincmd t
let s:save_winminheight = &winminheight
let s:save_winminwidth = &winminwidth
set winminheight=0
set winheight=1
set winminwidth=0
set winwidth=1
exe 'vert 1resize ' . ((&columns * 96 + 97) / 194)
exe 'vert 2resize ' . ((&columns * 97 + 97) / 194)
argglobal
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let &fdl = &fdl
let s:l = 6 - ((5 * winheight(0) + 23) / 46)
if s:l < 1 | let s:l = 1 | endif
keepjumps exe s:l
normal! zt
keepjumps 6
normal! 0
wincmd w
argglobal
enew
file \[scnvim]
balt ~/TonalTheory/scratch.scd
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal nofen
wincmd w
exe 'vert 1resize ' . ((&columns * 96 + 97) / 194)
exe 'vert 2resize ' . ((&columns * 97 + 97) / 194)
tabnext 2
set stal=1
if exists('s:wipebuf') && len(win_findbuf(s:wipebuf)) == 0 && getbufvar(s:wipebuf, '&buftype') isnot# 'terminal'
  silent exe 'bwipe ' . s:wipebuf
endif
unlet! s:wipebuf
set winheight=1 winwidth=20
let &shortmess = s:shortmess_save
let &winminheight = s:save_winminheight
let &winminwidth = s:save_winminwidth
let s:sx = expand("<sfile>:p:r")."x.vim"
if filereadable(s:sx)
  exe "source " . fnameescape(s:sx)
endif
let &g:so = s:so_save | let &g:siso = s:siso_save
set hlsearch
nohlsearch
doautoall SessionLoadPost
unlet SessionLoad
" vim: set ft=vim :
