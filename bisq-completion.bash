#!/usr/bin/env bash
#
# bisq Bash Completion
# =======================
#
# Bash completion support for the `bisq` command,
# generated by [picocli](http://picocli.info/) version 4.6.1.
#
# Installation
# ------------
#
# 1. Source all completion scripts in your .bash_profile
#
#   cd $YOUR_APP_HOME/bin
#   for f in $(find . -name "*_completion"); do line=". $(pwd)/$f"; grep "$line" ~/.bash_profile || echo "$line" >> ~/.bash_profile; done
#
# 2. Open a new bash console, and type `bisq [TAB][TAB]`
#
# 1a. Alternatively, if you have [bash-completion](https://github.com/scop/bash-completion) installed:
#     Place this file in a `bash-completion.d` folder:
#
#   * /etc/bash-completion.d
#   * /usr/local/etc/bash-completion.d
#   * ~/bash-completion.d
#
# Documentation
# -------------
# The script is called by bash whenever [TAB] or [TAB][TAB] is pressed after
# 'bisq (..)'. By reading entered command line parameters,
# it determines possible bash completions and writes them to the COMPREPLY variable.
# Bash then completes the user input if only one entry is listed in the variable or
# shows the options if more than one is listed in COMPREPLY.
#
# References
# ----------
# [1] http://stackoverflow.com/a/12495480/1440785
# [2] http://tiswww.case.edu/php/chet/bash/FAQ
# [3] https://www.gnu.org/software/bash/manual/html_node/The-Shopt-Builtin.html
# [4] http://zsh.sourceforge.net/Doc/Release/Options.html#index-COMPLETE_005fALIASES
# [5] https://stackoverflow.com/questions/17042057/bash-check-element-in-array-for-elements-in-another-array/17042655#17042655
# [6] https://www.gnu.org/software/bash/manual/html_node/Programmable-Completion.html#Programmable-Completion
# [7] https://stackoverflow.com/questions/3249432/can-a-bash-tab-completion-script-be-used-in-zsh/27853970#27853970
#

if [ -n "$BASH_VERSION" ]; then
  # Enable programmable completion facilities when using bash (see [3])
  shopt -s progcomp
elif [ -n "$ZSH_VERSION" ]; then
  # Make alias a distinct command for completion purposes when using zsh (see [4])
  setopt COMPLETE_ALIASES
  alias compopt=complete

  # Enable bash completion in zsh (see [7])
  autoload -U +X compinit && compinit
  autoload -U +X bashcompinit && bashcompinit
fi

# CompWordsContainsArray takes an array and then checks
# if all elements of this array are in the global COMP_WORDS array.
#
# Returns zero (no error) if all elements of the array are in the COMP_WORDS array,
# otherwise returns 1 (error).
function CompWordsContainsArray() {
  declare -a localArray
  localArray=("$@")
  local findme
  for findme in "${localArray[@]}"; do
    if ElementNotInCompWords "$findme"; then return 1; fi
  done
  return 0
}
function ElementNotInCompWords() {
  local findme="$1"
  local element
  for element in "${COMP_WORDS[@]}"; do
    if [[ "$findme" = "$element" ]]; then return 1; fi
  done
  return 0
}

# The `currentPositionalIndex` function calculates the index of the current positional parameter.
#
# currentPositionalIndex takes three parameters:
# the command name,
# a space-separated string with the names of options that take a parameter, and
# a space-separated string with the names of boolean options (that don't take any params).
# When done, this function echos the current positional index to std_out.
#
# Example usage:
# local currIndex=$(currentPositionalIndex "mysubcommand" "$ARG_OPTS" "$FLAG_OPTS")
function currentPositionalIndex() {
  local commandName="$1"
  local optionsWithArgs="$2"
  local booleanOptions="$3"
  local previousWord
  local result=0

  for i in $(seq $((COMP_CWORD - 1)) -1 0); do
    previousWord=${COMP_WORDS[i]}
    if [ "${previousWord}" = "$commandName" ]; then
      break
    fi
    if [[ "${optionsWithArgs}" =~ ${previousWord} ]]; then
      ((result-=2)) # Arg option and its value not counted as positional param
    elif [[ "${booleanOptions}" =~ ${previousWord} ]]; then
      ((result-=1)) # Flag option itself not counted as positional param
    fi
    ((result++))
  done
  echo "$result"
}

# Bash completion entry point function.
# _complete_bisq finds which commands and subcommands have been specified
# on the command line and delegates to the appropriate function
# to generate possible options and subcommands for the last specified subcommand.
function _complete_bisq() {
  local cmds0=(price)
  local cmds1=(offer)
  local cmds2=(offer create)
  local cmds3=(offer delete)
  local cmds4=(offer list)
  local cmds5=(offer view)

  if CompWordsContainsArray "${cmds5[@]}"; then _picocli_bisq_offer_view; return $?; fi
  if CompWordsContainsArray "${cmds4[@]}"; then _picocli_bisq_offer_list; return $?; fi
  if CompWordsContainsArray "${cmds3[@]}"; then _picocli_bisq_offer_delete; return $?; fi
  if CompWordsContainsArray "${cmds2[@]}"; then _picocli_bisq_offer_create; return $?; fi
  if CompWordsContainsArray "${cmds1[@]}"; then _picocli_bisq_offer; return $?; fi
  if CompWordsContainsArray "${cmds0[@]}"; then _picocli_bisq_price; return $?; fi

  # No subcommands were specified; generate completions for the top-level command.
  _picocli_bisq; return $?;
}

# Generates completions for the options and subcommands of the `bisq` command.
function _picocli_bisq() {
  # Get completion data
  local curr_word=${COMP_WORDS[COMP_CWORD]}

  local commands="price offer"
  local flag_opts="--debug"
  local arg_opts=""

  if [[ "${curr_word}" == -* ]]; then
    COMPREPLY=( $(compgen -W "${flag_opts} ${arg_opts}" -- "${curr_word}") )
  else
    local positionals=""
    COMPREPLY=( $(compgen -W "${commands} ${positionals}" -- "${curr_word}") )
  fi
}

# Generates completions for the options and subcommands of the `price` subcommand.
function _picocli_bisq_price() {
  # Get completion data
  local curr_word=${COMP_WORDS[COMP_CWORD]}

  local commands=""
  local flag_opts=""
  local arg_opts=""

  if [[ "${curr_word}" == -* ]]; then
    COMPREPLY=( $(compgen -W "${flag_opts} ${arg_opts}" -- "${curr_word}") )
  else
    local positionals=""
    COMPREPLY=( $(compgen -W "${commands} ${positionals}" -- "${curr_word}") )
  fi
}

# Generates completions for the options and subcommands of the `offer` subcommand.
function _picocli_bisq_offer() {
  # Get completion data
  local curr_word=${COMP_WORDS[COMP_CWORD]}

  local commands="create delete list view"
  local flag_opts=""
  local arg_opts=""

  if [[ "${curr_word}" == -* ]]; then
    COMPREPLY=( $(compgen -W "${flag_opts} ${arg_opts}" -- "${curr_word}") )
  else
    local positionals=""
    COMPREPLY=( $(compgen -W "${commands} ${positionals}" -- "${curr_word}") )
  fi
}

# Generates completions for the options and subcommands of the `create` subcommand.
function _picocli_bisq_offer_create() {
  # Get completion data
  local curr_word=${COMP_WORDS[COMP_CWORD]}

  local commands=""
  local flag_opts=""
  local arg_opts=""

  if [[ "${curr_word}" == -* ]]; then
    COMPREPLY=( $(compgen -W "${flag_opts} ${arg_opts}" -- "${curr_word}") )
  else
    local positionals=""
    COMPREPLY=( $(compgen -W "${commands} ${positionals}" -- "${curr_word}") )
  fi
}

# Generates completions for the options and subcommands of the `delete` subcommand.
function _picocli_bisq_offer_delete() {
  # Get completion data
  local curr_word=${COMP_WORDS[COMP_CWORD]}

  local commands=""
  local flag_opts=""
  local arg_opts=""

  if [[ "${curr_word}" == -* ]]; then
    COMPREPLY=( $(compgen -W "${flag_opts} ${arg_opts}" -- "${curr_word}") )
  else
    local positionals=""
    COMPREPLY=( $(compgen -W "${commands} ${positionals}" -- "${curr_word}") )
  fi
}

# Generates completions for the options and subcommands of the `list` subcommand.
function _picocli_bisq_offer_list() {
  # Get completion data
  local curr_word=${COMP_WORDS[COMP_CWORD]}

  local commands=""
  local flag_opts=""
  local arg_opts=""

  if [[ "${curr_word}" == -* ]]; then
    COMPREPLY=( $(compgen -W "${flag_opts} ${arg_opts}" -- "${curr_word}") )
  else
    local positionals=""
    COMPREPLY=( $(compgen -W "${commands} ${positionals}" -- "${curr_word}") )
  fi
}

# Generates completions for the options and subcommands of the `view` subcommand.
function _picocli_bisq_offer_view() {
  # Get completion data
  local curr_word=${COMP_WORDS[COMP_CWORD]}

  local commands=""
  local flag_opts=""
  local arg_opts=""

  if [[ "${curr_word}" == -* ]]; then
    COMPREPLY=( $(compgen -W "${flag_opts} ${arg_opts}" -- "${curr_word}") )
  else
    local positionals=""
    COMPREPLY=( $(compgen -W "${commands} ${positionals}" -- "${curr_word}") )
  fi
}

# Define a completion specification (a compspec) for the
# `bisq`, `bisq.sh`, and `bisq.bash` commands.
# Uses the bash `complete` builtin (see [6]) to specify that shell function
# `_complete_bisq` is responsible for generating possible completions for the
# current word on the command line.
# The `-o default` option means that if the function generated no matches, the
# default Bash completions and the Readline default filename completions are performed.
complete -F _complete_bisq -o default bisq bisq.sh bisq.bash
