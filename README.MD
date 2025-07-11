## Share To File

Share To File is a simple application I cobbled together to fulfill a need I had for my workflow.

I keep track of my life, besides tasks, via a file I call obtf.md (One Big Text File). It holds any and all of my notes,
including books to read, snippets of code etc.

Each day that I add to it, I add a date stamp to the top of the file and add any relevant notes underneath of it.

Share To File allows you to share text to the application via the Android Share menu. You can then pick
a plain text file (such as .txt, .md etc) and the shared text will get written to the top of the file with a date stamp above it.

That's about it.

## TODO
* Settings
  * Append text to top or bottom of file
  * Append text with a date stamp or not
  * Format of the date stamp (if you are choosing to use it)
* Prettying up the UI although the UI will be pretty bar as it will really just be a settings page
* Specify a default file
  * Tell user that file A is the default and give them the ability, if a default is set to instead append to a non default file as a one-off
* Detection as to whether a date stamp for today already exists and if it does, do not duplicate the date stamp. In this scenario, if you had

```sh 
20250611

Note A
Note B
```

and you shared "Note C", you would end up with

```sh
20250611

Note A
Note B
Note C

20250610
etc
etc
```

## Contributing
I am a C#/.NET developer by trade so I am sure there are some best practices not being followed although I am trying my best.

Right now, this application is only for my use but if you feel compelled, feel free to send a PR and I will review it :)
