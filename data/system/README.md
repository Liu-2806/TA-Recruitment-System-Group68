# System Data

Purpose:

- store small shared reference data used across the system

Possible contents:

- department options
- academic year options
- static role options
- system settings if needed later
- TA timetable and posting schedule helper data

Current TA module usage:

- `ta-timetable.json` stores weekly TA timetable rows keyed by `taId + weekStart`
- posting schedule rows keyed by `postingId` support timetable conflict checks before TA application submission
- accepted-application withdrawal can release the linked timetable assignment blocks
- revocation requests now also return the user to the TA applications page with visible feedback

