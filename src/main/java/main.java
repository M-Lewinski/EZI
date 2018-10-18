import com.beust.jcommander.JCommander;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class main {

    public static void main(String[] args){
        CommandParameters command = new CommandParameters();
        JCommander.newBuilder()
                .addObject(command)
                .build()
                .parse(args);

        VectorSpace vectorSpace = new VectorSpace(command);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            try {
                System.out.printf("\nEnter querry\n");
                String querryString = br.readLine();
                List<Ranking> rankingList = vectorSpace.querryExecution(querryString);
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
