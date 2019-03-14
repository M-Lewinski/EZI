package com.tfidf;

import com.beust.jcommander.Parameter;

public class CommandParameters {

    @Parameter(names = {"--document","-d"}, description = "name of a file containing all documents title and body", required = true)
    public String documentFileName = null;

    @Parameter(names = {"--keywords","-k"}, description = "name of a file containing all the keywords that will be considered while analyzing documents and querry", required = true)
    public String keywordsFileName = null;

    @Parameter(names = {"--verbose","-v"}, description = "Run program in verbose mode. It will print more details than normally")
    public boolean verbose = false;


    @Parameter(names = {"--help","-h"}, description = "Shows program usage", help = true)
    public boolean help = false;

}
