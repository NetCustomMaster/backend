package com.yc.rtu.netcustommaster.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandExecutor {
    public static String executeCommand(String command) {
        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});

            // Read standard output
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();

            // Read error output
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }
            errorReader.close();

            // Wait for process to complete
            process.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // Log error output if there's any, for debugging
        if (errorOutput.length() > 0) {
            System.err.println("Error output: " + errorOutput.toString().trim());
        }

        return output.toString().trim();
    }

}
