package mo.bioinf.bmark;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class ByteReader {

    private boolean has_next_line;
    private boolean second_to_last = false;

    private String next = "";

    private Queue<File> solids_queue = new LinkedList<File>();
    private byte[] current_bytes;
    private int byte_index = 0;


    private String fastq_name;
    private String basepath;



    ByteReader(String fastq_name,String basepath)
    {
        this.fastq_name = fastq_name;
        this.basepath = basepath;

        find_solids();

        load_bytes();

        this.next = get_2_bytes();



    }

    ByteReader(){}

    public String getNext()
    {
        if(!has_next_line)
        {
            return "error";
        }

        String ans = this.next;

        if(index_at_end()){
            if(this.solids_queue.isEmpty())
            {
                this.has_next_line = false;
                return ans;
            }else{
                load_bytes();
            }
        }


        this.next = get_2_bytes();








        return ans;
    }

    public boolean hasNext()
    {
        return this.has_next_line;
    }

    private boolean index_at_end()
    {
        if(this.byte_index == this.current_bytes.length)
            return true;
        else
            return false;
    }

    /**
     * assuming that we can get 2 bytes from the array
     * @return
     */
    public String get_2_bytes()
    {

        StringBuilder ans = new StringBuilder("");

        //byte[] bytes = new byte[2];

        StringBuilder temp_hex = new StringBuilder("");
        for(int i = 0; i < 2; i++)
        {
            temp_hex.setLength(0);
            byte current_byte = this.current_bytes[this.byte_index];

            temp_hex.append(Integer.toHexString((int)current_byte));

            if(temp_hex.length() > 2)
            {
                unNegative(temp_hex);
            }else{
                extend(temp_hex,2);
            }

            this.byte_index++;

            ans.append(temp_hex);


        }



        return ans.toString();

    }

//    private boolean check_end_of_bytes()
//    {
//
//        if(this.byte_index == (this.current_bytes.length -1)) // exhausted all bytes from this file
//        {
//
//        }
//
//    }

    private void load_bytes()
    {
        this.byte_index = 0;

        this.current_bytes = readBytes(this.solids_queue.remove());


    }

    /**
     * Java assumes all bytes read are in 2's complement, so if the binary form of any byte starts with a 1,
     * Java treats it as a negative number. DSK wrote the binaries as unsigned binary numbers, so this needs to be undone.
     *
     * Java sign extends the bytes, so they are all strings of 8 length when this happens, so we can
     * just remove the first 6 characters from the string to remove the sign extension.
     *
     * @param input
     * @return
     */
    public void unNegative(StringBuilder input)
    {
        input.delete(0,6);

    }

    public static void extend(StringBuilder input, int goalSize){
        int length = input.length();

        int difference = goalSize - length;

        //String original = input.toString();

        //StringBuilder substr = new StringBuilder("");

        for(int i = 0; i < difference; i++)
        {
            input.insert(0,'0');
        }

        //return substr.append(input).toString();


    }

    private byte[] readBytes(File input){
        byte[] ans;
        try{

            long filesize = input.length();
            InputStream inputstream = new FileInputStream(input);
            byte[] allBytes = new byte[(int) filesize];



            inputstream.read(allBytes);


            inputstream.close();

            return allBytes;

        }catch(java.io.IOException e){
            System.out.println(e.getMessage());
        }

        return null;


    }

    private void find_solids()
    {
        int count = 0;


        while(true)
        {
            String path = this.basepath + this.fastq_name + "_gatb/dsk.solid." + count;
            File file = new File(path);
            if(file.exists() && file.canRead())
            {
                this.solids_queue.add(file);
                this.has_next_line = true;
                count++;
            }else{
                System.out.println("could not find " + path);
                break;
            }

        }

    }

}
