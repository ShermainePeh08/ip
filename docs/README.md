# 🐸 Shonks User Guide

<img src="Ui.png" width="300" alt="Shonks Demo Ui">

## Meet Shonks

Ribbit.

Shonks is a grumpy but efficient task manager who helps you organise your life — reluctantly.

Unlike boring productivity apps, Shonks lives in a chat style interface and responds to your commands directly. Add tasks, track deadlines, archive completed work, and even visualise your workload with a pie chart.

He may complain.  
But he *will* keep your tasks in order.

---

## Features at a Glance

- ➕ **Add tasks**: Todo, Deadline, Event  
- 📋 **List tasks**: View everything in your task list  
- ✔ **Mark / Unmark tasks**: Track completion status  
- 🗑 **Delete tasks**: Remove tasks you no longer need  
- 🔍 **Finding tasks**: Find tasks containing a keyword  
- 📊 **Stats (with Pie Chart)**: Visual breakdown of task types  
- 🗄 **Archive tasks**: Move tasks out of your main list  
- 🔄 **Restore archive**: Bring archived tasks back  
- 👋 **Exit with style**: Shonks says goodbye before closing  

## Quick Start

1. Download the `.jar` file.
2. Run: `java -jar shonks.jar`
3. Type commands into the input bar.
4. Press Enter or click Send.

Shonks will respond immediately.

---
# Command Summary

| Action | Command |
|--------|---------|
| Add Todo | `todo <description>` |
| Add Deadline | `deadline <description> /by <date/time>` |
| Add Event | `event <description> /from <date/time> /to <date/time>` |
| List Tasks | `list` |
| Mark Task | `mark <index>` |
| Unmark Task | `unmark <index>` |
| Delete Task | `delete <index>` |
| Find Task | `find <keyword>` |
| Show Stats | `stats` |
| Archive | `archive` / `archive <index>` |
| Restore | `restore` / `restore <index>` |
| Exit | `bye` |

---

# Commands

## Adding Tasks

### Todo

Format: `todo <description>`

Example: `todo read book`

---

### Deadline

Format: `deadline <description> /by <date/time>`

Example: `deadline submit report /by 2026-02-20 1800`

If the date format is invalid, Shonks will display an error message.

---

### Event

Format: `event <description> /from <start date/time> /to <end date/time>`

Example:  
`event project meeting /from 2026-02-20 1400 /to 2026-02-20 1600`

---

## Listing Tasks

Format: `list`

Displays all tasks currently in your task list.  
Tasks are numbered starting from 1.

---

## Marking Tasks

Mark a task as done: `mark <index>`  
Unmark a task: `unmark <index>`

Example:  
`mark 1`  
`unmark 2`

---

## Deleting Tasks

Format: `delete <index>`

Example: `delete 3`

---

## Finding Tasks

Format: `find <keyword>`

Example: `find report`

Shonks will display matching tasks.

---

## Stats (with Pie Chart)

Format: `stats`

Shonks will display a pie chart directly inside the chat window, showing the breakdown of Todo, Deadline, and Event tasks.

---

## Archive Tasks

Archive all tasks: `archive`  
Archive a specific task: `archive <index>`

Archived tasks are removed from your main list and stored separately.

---

## Restore Archive

Restore all archived tasks: `restore`  
Restore a specific archived task: `restore <index>`

Restored tasks will reappear in your main task list.

---

## Exiting Shonks

Format: `bye`

Shonks will print a goodbye message, pause briefly, and then close the application.

---

Stay organised.

Shonks is watching. 🐸
