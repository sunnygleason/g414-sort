# Overview

Library/framework for basic merge sort on Java.

While it seems like a simple problem to solve there is plenty of complexity due to multiple input sources (Files, network, streams), output sources (output stream, file etc), as well as from two distinct phases of sorting: pre-sorting and main merge-sorts.
On top of this, input is not merely assumed to be lines of text but can be typed data; interfaces allow arbitrary abstractions to be used.

In addition to providing abstractions for plugging in various sources, destinations and data formats, package provides some basic implementations for common cases (reading and sorting line-based text, for example).



