/*

noble-sermon : Interpreter for Custom Language

Copyright (c) 2019 Queued Pixel <git@queuedpixel.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/

package com.queuedpixel.noblesermon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Main
{
    public static void main( String[] args ) throws Exception
    {
        System.out.println( "Welcome to Noble-Sermon." );
        System.out.println();

        BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ));
        System.out.print( "> " );
        String line = reader.readLine();

        while ( line != null )
        {
            List< Token > tokens = Main.tokenize( line );
            for ( Token token : tokens )
            {
                System.out.println( "Token: " + token.toString() );
            }
            System.out.println( line );
            System.out.print( "> " );
            line = reader.readLine();
        }
    }

    private static List< Token > tokenize( String s )
    {
        // add whitespace to the end to allow us to handle closing tokens
        s += " ";

        // initialize our starting state
        List< Token > tokens = new LinkedList<>();
        int index = 0;
        int state = 0;
        StringBuilder token = new StringBuilder();

        // iterate through every character in the string
        while ( index < s.length() )
        {
            int codePoint = s.codePointAt( index );

            switch ( state )
            {
                // no current token
                case 0:
                    if ( Character.isWhitespace( codePoint )) index++;
                    else if ( Character.isDigit( codePoint )) state = 1;
                    else if ( codePoint == '+' )
                    {
                        tokens.add( new PlusToken() );
                        index++;
                    }
                    else throw new IllegalStateException(
                            "Unrecognized Character: '" + Main.codePointToString( codePoint ) + "'" );
                    break;

                // processing integer
                case 1:
                    if ( Character.isDigit( codePoint ))
                    {
                        token.appendCodePoint( codePoint );
                        index++;
                    }
                    else
                    {
                        tokens.add( new IntegerToken( Integer.parseInt( token.toString() )));
                        state = 0;
                        token = new StringBuilder();
                    }
                    break;

                // unrecognized state
                default : throw new IllegalStateException( "Unrecognized State: " + state );
            }
        }

        return tokens;
    }

    private static String codePointToString( int codePoint )
    {
        StringBuilder builder = new StringBuilder();
        builder.appendCodePoint( codePoint );
        return builder.toString();
    }
}
