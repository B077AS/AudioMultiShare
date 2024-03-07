# AudioMultiShare
Audio Multi-Share is a JavaFX-based application designed for routing audio from a selected input source to two distinct output devices simultaneously. The program utilizes Java Sound API for capturing and rendering audio streams, providing users with a straightforward solution for sharing audio across multiple speakers.

Users can choose a virtual cable input source from the available options.
Speaker 1 and Speaker 2: Users can independently select two output devices for routing the audio.

Routing Controls:

Start: Initiates the audio routing process, directing the input source to both selected output devices.

Stop: Halts the audio routing, allowing users to discontinue the sharing process.

Refresh: Updates the list of available audio devices, ensuring users have the latest options for selection.



Audio Routing Logic:

Utilizes the Java Sound API to manage audio format, capturing from a source and playing back on two distinct output devices simultaneously.
