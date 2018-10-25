package com.lab1;

import com.beust.jcommander.JCommander;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args){
        CommandParameters command = new CommandParameters();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(command)
                .build();
        jCommander.setProgramName("EZI");
        try
        {
            jCommander.parse(args);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            jCommander.usage();
            return;
        }
        if (command.help){
            jCommander.usage();
            return;
        }
        VectorSpace vectorSpace = new VectorSpace(command);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try {
                System.out.printf("\nEnter querry\n");
                String querryString = br.readLine();
                Querry querry = vectorSpace.convertStringToQuerry(querryString);
                List<Ranking> rankingList = vectorSpace.querryExecution(querry);
                for (Ranking ranking: rankingList) {
                    System.out.printf("%f : %s\n",ranking.simIdf,ranking.document.title);
                }
                System.out.printf("\nDo you want to Rochio to give feedback about relevant and nonrelevant documents [Y/n]\n");
                String rochio = br.readLine();
                if (rochio.equals("n")){
                    continue;
                }
                Map<Document,Boolean> feedback = new HashMap<>();
                for (int i = 0; i < 10 && i < rankingList.size(); i++){
                    Document doc = rankingList.get(i).document;
                    System.out.println("Is this document relevant [y/n/enter to skip]");
                    System.out.println(doc.title);
                    String relevant = br.readLine();
                    if (relevant.equals("y")){
                        feedback.put(doc,true);
                    }
                    else if (relevant.equals("n")) {
                        feedback.put(doc,false);
                    }
                }
                querry.tf = vectorSpace.rochio(querry.bagOfWords,feedback);
                rankingList = vectorSpace.querryExecution(querry);
                for (Ranking ranking: rankingList) {
                    System.out.printf("%f : %s\n",ranking.simIdf,ranking.document.title);
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Couldn't read querry from console");
                System.exit(1);
            }
        }

    }

}
