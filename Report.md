# Final Report
Jarrod Polen
jpp52@zips.uakron.edu

## Comparison

Jenkins and CircleCi essentially have the same functionallity, and their major difference comes from ease of use. CircleCi allows for easier implementation when it comes to small teams and does not require much knowledge of Docker while Jenkins requires a greater understanding of it. The downside of CircleCi is that it gives slightly less control than Docker and has slightly longer build times due to it automatically running after every push from GitHub. Speaking of Github, CircleCi is also allows easy integration with Github. The automatic builds however can be a nice feature as Jenkins requires manual builds. The final major difference comes from sharing with team members. CircleCi allows you to share the workflow with a link while Jenkins requires you to host the Jenkins container on a server which will require more work.

## My Features

### Ansi Color
Ansi Color is plugin that adds support for standard ANSI escape sequences, including color, to Console Output. In my Jenkins Pipeline, I used it to highlight the different stages throughout the pipeline. When a user reads the console output, they will be able to locate particular stages easier by seeing the colored text.

### Archive Artifacts
Archive Artifacts allows build artifacts to be stored away to be downloaded later. Archived files are accessible from the Jenkins webpage as long as the build remains. I used this in the project to archive the .deb package on a success of the stages to be able to download later.