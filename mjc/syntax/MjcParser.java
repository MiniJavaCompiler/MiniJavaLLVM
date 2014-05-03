// Output created by jacc on Sun Feb 17 00:58:01 PST 2008

package syntax;

import compiler.Phase;
import compiler.Handler;
import compiler.Failure;
import compiler.Warning;
import compiler.Position;
import java.util.Vector;
import lexer.MjcLexer;

class MjcParser extends Phase implements MjcTokens {
    private int yyss = 100;
    private int yytok;
    private int yysp = 0;
    private int[] yyst;
    protected int yyerrno = (-1);
    private Object[] yysv;
    private Object yyrv;

    public boolean parse() {
        int yyn = 0;
        yysp = 0;
        yyst = new int[yyss];
        yysv = new Object[yyss];
        yytok = (lexer.getToken()
                 );
    loop:
        for (;;) {
            switch (yyn) {
                case 0:
                    yyst[yysp] = 0;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 174:
                    yyn = yys0();
                    continue;

                case 1:
                    yyst[yysp] = 1;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 175:
                    switch (yytok) {
                        case ENDINPUT:
                            yyn = 348;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 2:
                    yyst[yysp] = 2;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 176:
                    yyn = yys2();
                    continue;

                case 3:
                    yyst[yysp] = 3;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 177:
                    yyn = yys3();
                    continue;

                case 4:
                    yyst[yysp] = 4;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 178:
                    yyn = yys4();
                    continue;

                case 5:
                    yyst[yysp] = 5;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 179:
                    yyn = yys5();
                    continue;

                case 6:
                    yyst[yysp] = 6;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 180:
                    yyn = yys6();
                    continue;

                case 7:
                    yyst[yysp] = 7;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 181:
                    switch (yytok) {
                        case IDENT:
                            yyn = 12;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 8:
                    yyst[yysp] = 8;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 182:
                    yyn = yys8();
                    continue;

                case 9:
                    yyst[yysp] = 9;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 183:
                    yyn = yys9();
                    continue;

                case 10:
                    yyst[yysp] = 10;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 184:
                    yyn = yys10();
                    continue;

                case 11:
                    yyst[yysp] = 11;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 185:
                    yyn = yys11();
                    continue;

                case 12:
                    yyst[yysp] = 12;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 186:
                    switch (yytok) {
                        case EXTENDS:
                            yyn = 14;
                            continue;
                        case '{':
                            yyn = yyr5();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 13:
                    yyst[yysp] = 13;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 187:
                    switch (yytok) {
                        case '{':
                            yyn = 15;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 14:
                    yyst[yysp] = 14;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 188:
                    switch (yytok) {
                        case IDENT:
                            yyn = 17;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 15:
                    yyst[yysp] = 15;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 189:
                    yyn = yys15();
                    continue;

                case 16:
                    yyst[yysp] = 16;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 190:
                    switch (yytok) {
                        case '.':
                            yyn = 19;
                            continue;
                        case '{':
                            yyn = yyr6();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 17:
                    yyst[yysp] = 17;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 191:
                    yyn = yys17();
                    continue;

                case 18:
                    yyst[yysp] = 18;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 192:
                    yyn = yys18();
                    continue;

                case 19:
                    yyst[yysp] = 19;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 193:
                    switch (yytok) {
                        case IDENT:
                            yyn = 23;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 20:
                    yyst[yysp] = 20;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 194:
                    yyn = yys20();
                    continue;

                case 21:
                    yyst[yysp] = 21;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 195:
                    yyn = yys21();
                    continue;

                case 22:
                    yyst[yysp] = 22;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 196:
                    yyn = yys22();
                    continue;

                case 23:
                    yyst[yysp] = 23;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 197:
                    yyn = yys23();
                    continue;

                case 24:
                    yyst[yysp] = 24;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 198:
                    switch (yytok) {
                        case '.':
                            yyn = 19;
                            continue;
                        case IDENT:
                            yyn = yyr21();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 25:
                    yyst[yysp] = 25;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 199:
                    switch (yytok) {
                        case IDENT:
                            yyn = 31;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 26:
                    yyst[yysp] = 26;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 200:
                    switch (yytok) {
                        case IDENT:
                            yyn = yyr20();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 27:
                    yyst[yysp] = 27;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 201:
                    switch (yytok) {
                        case IDENT:
                            yyn = yyr19();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 28:
                    yyst[yysp] = 28;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 202:
                    switch (yytok) {
                        case IDENT:
                            yyn = 32;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 29:
                    yyst[yysp] = 29;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 203:
                    switch (yytok) {
                        case ';':
                        case ',':
                            yyn = yyr25();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 30:
                    yyst[yysp] = 30;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 204:
                    switch (yytok) {
                        case ',':
                            yyn = 33;
                            continue;
                        case ';':
                            yyn = 34;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 31:
                    yyst[yysp] = 31;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 205:
                    switch (yytok) {
                        case '(':
                            yyn = 35;
                            continue;
                        case ';':
                        case ',':
                            yyn = yyr26();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 32:
                    yyst[yysp] = 32;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 206:
                    switch (yytok) {
                        case '(':
                            yyn = 36;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 33:
                    yyst[yysp] = 33;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 207:
                    switch (yytok) {
                        case IDENT:
                            yyn = 38;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 34:
                    yyst[yysp] = 34;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 208:
                    yyn = yys34();
                    continue;

                case 35:
                    yyst[yysp] = 35;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 209:
                    switch (yytok) {
                        case IDENT:
                            yyn = 17;
                            continue;
                        case BOOLEAN:
                            yyn = 26;
                            continue;
                        case INT:
                            yyn = 27;
                            continue;
                        case ')':
                            yyn = yyr28();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 36:
                    yyst[yysp] = 36;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 210:
                    switch (yytok) {
                        case IDENT:
                            yyn = 17;
                            continue;
                        case BOOLEAN:
                            yyn = 26;
                            continue;
                        case INT:
                            yyn = 27;
                            continue;
                        case ')':
                            yyn = yyr28();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 37:
                    yyst[yysp] = 37;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 211:
                    switch (yytok) {
                        case ';':
                        case ',':
                            yyn = yyr24();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 38:
                    yyst[yysp] = 38;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 212:
                    switch (yytok) {
                        case ';':
                        case ',':
                            yyn = yyr26();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 39:
                    yyst[yysp] = 39;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 213:
                    switch (yytok) {
                        case ',':
                        case ')':
                            yyn = yyr30();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 40:
                    yyst[yysp] = 40;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 214:
                    switch (yytok) {
                        case ')':
                            yyn = 44;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 41:
                    yyst[yysp] = 41;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 215:
                    switch (yytok) {
                        case ',':
                            yyn = 45;
                            continue;
                        case ')':
                            yyn = yyr27();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 42:
                    yyst[yysp] = 42;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 216:
                    switch (yytok) {
                        case IDENT:
                            yyn = 46;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 43:
                    yyst[yysp] = 43;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 217:
                    switch (yytok) {
                        case ')':
                            yyn = 47;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 44:
                    yyst[yysp] = 44;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 218:
                    switch (yytok) {
                        case ';':
                            yyn = 50;
                            continue;
                        case '{':
                            yyn = 51;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 45:
                    yyst[yysp] = 45;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 219:
                    switch (yytok) {
                        case IDENT:
                            yyn = 17;
                            continue;
                        case BOOLEAN:
                            yyn = 26;
                            continue;
                        case INT:
                            yyn = 27;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 46:
                    yyst[yysp] = 46;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 220:
                    switch (yytok) {
                        case ',':
                        case ')':
                            yyn = yyr31();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 47:
                    yyst[yysp] = 47;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 221:
                    switch (yytok) {
                        case ';':
                            yyn = 50;
                            continue;
                        case '{':
                            yyn = 51;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 48:
                    yyst[yysp] = 48;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 222:
                    yyn = yys48();
                    continue;

                case 49:
                    yyst[yysp] = 49;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 223:
                    yyn = yys49();
                    continue;

                case 50:
                    yyst[yysp] = 50;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 224:
                    yyn = yys50();
                    continue;

                case 51:
                    yyst[yysp] = 51;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 225:
                    yyn = yys51();
                    continue;

                case 52:
                    yyst[yysp] = 52;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 226:
                    switch (yytok) {
                        case ',':
                        case ')':
                            yyn = yyr29();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 53:
                    yyst[yysp] = 53;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 227:
                    yyn = yys53();
                    continue;

                case 54:
                    yyst[yysp] = 54;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 228:
                    yyn = yys54();
                    continue;

                case 55:
                    yyst[yysp] = 55;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 229:
                    switch (yytok) {
                        case ';':
                            yyn = yyr46();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 56:
                    yyst[yysp] = 56;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 230:
                    yyn = yys56();
                    continue;

                case 57:
                    yyst[yysp] = 57;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 231:
                    switch (yytok) {
                        case ';':
                            yyn = yyr44();
                            continue;
                        case '.':
                            yyn = yyr85();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 58:
                    yyst[yysp] = 58;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 232:
                    switch (yytok) {
                        case ';':
                            yyn = 81;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 59:
                    yyst[yysp] = 59;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 233:
                    yyn = yys59();
                    continue;

                case 60:
                    yyst[yysp] = 60;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 234:
                    switch (yytok) {
                        case '=':
                            yyn = 82;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 61:
                    yyst[yysp] = 61;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 235:
                    yyn = yys61();
                    continue;

                case 62:
                    yyst[yysp] = 62;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 236:
                    switch (yytok) {
                        case '.':
                            yyn = 19;
                            continue;
                        case '(':
                            yyn = 83;
                            continue;
                        case '=':
                            yyn = yyr53();
                            continue;
                        case IDENT:
                            yyn = yyr21();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 63:
                    yyst[yysp] = 63;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 237:
                    switch (yytok) {
                        case ';':
                            yyn = yyr45();
                            continue;
                        case '.':
                            yyn = yyr83();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 64:
                    yyst[yysp] = 64;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 238:
                    switch (yytok) {
                        case '.':
                            yyn = 84;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 65:
                    yyst[yysp] = 65;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 239:
                    yyn = yys65();
                    continue;

                case 66:
                    yyst[yysp] = 66;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 240:
                    switch (yytok) {
                        case IDENT:
                            yyn = yyr18();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 67:
                    yyst[yysp] = 67;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 241:
                    yyn = yys67();
                    continue;

                case 68:
                    yyst[yysp] = 68;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 242:
                    yyn = yys68();
                    continue;

                case 69:
                    yyst[yysp] = 69;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 243:
                    switch (yytok) {
                        case '(':
                            yyn = 88;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 70:
                    yyst[yysp] = 70;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 244:
                    yyn = yys70();
                    continue;

                case 71:
                    yyst[yysp] = 71;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 245:
                    switch (yytok) {
                        case IDENT:
                            yyn = 17;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 72:
                    yyst[yysp] = 72;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 246:
                    yyn = yys72();
                    continue;

                case 73:
                    yyst[yysp] = 73;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 247:
                    yyn = yys73();
                    continue;

                case 74:
                    yyst[yysp] = 74;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 248:
                    switch (yytok) {
                        case '.':
                            yyn = 109;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 75:
                    yyst[yysp] = 75;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 249:
                    yyn = yys75();
                    continue;

                case 76:
                    yyst[yysp] = 76;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 250:
                    yyn = yys76();
                    continue;

                case 77:
                    yyst[yysp] = 77;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 251:
                    switch (yytok) {
                        case '(':
                            yyn = 110;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 78:
                    yyst[yysp] = 78;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 252:
                    yyn = yys78();
                    continue;

                case 79:
                    yyst[yysp] = 79;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 253:
                    yyn = yys79();
                    continue;

                case 80:
                    yyst[yysp] = 80;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 254:
                    yyn = yys80();
                    continue;

                case 81:
                    yyst[yysp] = 81;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 255:
                    yyn = yys81();
                    continue;

                case 82:
                    yyst[yysp] = 82;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 256:
                    yyn = yys82();
                    continue;

                case 83:
                    yyst[yysp] = 83;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 257:
                    yyn = yys83();
                    continue;

                case 84:
                    yyst[yysp] = 84;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 258:
                    switch (yytok) {
                        case IDENT:
                            yyn = 116;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 85:
                    yyst[yysp] = 85;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 259:
                    switch (yytok) {
                        case IDENT:
                            yyn = 38;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 86:
                    yyst[yysp] = 86;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 260:
                    switch (yytok) {
                        case '.':
                            yyn = 19;
                            continue;
                        case '(':
                            yyn = 83;
                            continue;
                        case '=':
                            yyn = yyr53();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 87:
                    yyst[yysp] = 87;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 261:
                    switch (yytok) {
                        case WHILE:
                            yyn = 118;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 88:
                    yyst[yysp] = 88;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 262:
                    yyn = yys88();
                    continue;

                case 89:
                    yyst[yysp] = 89;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 263:
                    switch (yytok) {
                        case '.':
                            yyn = 19;
                            continue;
                        case '(':
                            yyn = 120;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 90:
                    yyst[yysp] = 90;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 264:
                    yyn = yys90();
                    continue;

                case 91:
                    yyst[yysp] = 91;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 265:
                    switch (yytok) {
                        case ';':
                        case ',':
                        case ')':
                            yyn = yyr50();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 92:
                    yyst[yysp] = 92;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 266:
                    switch (yytok) {
                        case CAND:
                            yyn = 122;
                            continue;
                        case COR:
                        case ';':
                        case ',':
                        case ')':
                            yyn = yyr56();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 93:
                    yyst[yysp] = 93;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 267:
                    switch (yytok) {
                        case COR:
                            yyn = 123;
                            continue;
                        case ';':
                        case ',':
                        case ')':
                            yyn = yyr51();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 94:
                    yyst[yysp] = 94;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 268:
                    yyn = yys94();
                    continue;

                case 95:
                    yyst[yysp] = 95;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 269:
                    yyn = yys95();
                    continue;

                case 96:
                    yyst[yysp] = 96;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 270:
                    switch (yytok) {
                        case ';':
                            yyn = 126;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 97:
                    yyst[yysp] = 97;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 271:
                    yyn = yys97();
                    continue;

                case 98:
                    yyst[yysp] = 98;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 272:
                    yyn = yys98();
                    continue;

                case 99:
                    yyst[yysp] = 99;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 273:
                    yyn = yys99();
                    continue;

                case 100:
                    yyst[yysp] = 100;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 274:
                    yyn = yys100();
                    continue;

                case 101:
                    yyst[yysp] = 101;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 275:
                    yyn = yys101();
                    continue;

                case 102:
                    yyst[yysp] = 102;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 276:
                    yyn = yys102();
                    continue;

                case 103:
                    yyst[yysp] = 103;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 277:
                    yyn = yys103();
                    continue;

                case 104:
                    yyst[yysp] = 104;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 278:
                    yyn = yys104();
                    continue;

                case 105:
                    yyst[yysp] = 105;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 279:
                    yyn = yys105();
                    continue;

                case 106:
                    yyst[yysp] = 106;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 280:
                    yyn = yys106();
                    continue;

                case 107:
                    yyst[yysp] = 107;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 281:
                    yyn = yys107();
                    continue;

                case 108:
                    yyst[yysp] = 108;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 282:
                    yyn = yys108();
                    continue;

                case 109:
                    yyst[yysp] = 109;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 283:
                    switch (yytok) {
                        case IDENT:
                            yyn = 138;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 110:
                    yyst[yysp] = 110;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 284:
                    yyn = yys110();
                    continue;

                case 111:
                    yyst[yysp] = 111;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 285:
                    switch (yytok) {
                        case ')':
                            yyn = 140;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 112:
                    yyst[yysp] = 112;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 286:
                    switch (yytok) {
                        case ';':
                        case ',':
                        case ')':
                            yyn = yyr52();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 113:
                    yyst[yysp] = 113;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 287:
                    switch (yytok) {
                        case ')':
                            yyn = 141;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 114:
                    yyst[yysp] = 114;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 288:
                    switch (yytok) {
                        case ',':
                            yyn = 142;
                            continue;
                        case ')':
                            yyn = yyr97();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 115:
                    yyst[yysp] = 115;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 289:
                    switch (yytok) {
                        case ',':
                        case ')':
                            yyn = yyr99();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 116:
                    yyst[yysp] = 116;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 290:
                    yyn = yys116();
                    continue;

                case 117:
                    yyst[yysp] = 117;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 291:
                    switch (yytok) {
                        case ',':
                            yyn = 33;
                            continue;
                        case ';':
                            yyn = 144;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 118:
                    yyst[yysp] = 118;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 292:
                    switch (yytok) {
                        case '(':
                            yyn = 145;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 119:
                    yyst[yysp] = 119;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 293:
                    switch (yytok) {
                        case ')':
                            yyn = 146;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 120:
                    yyst[yysp] = 120;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 294:
                    switch (yytok) {
                        case ')':
                            yyn = 147;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 121:
                    yyst[yysp] = 121;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 295:
                    yyn = yys121();
                    continue;

                case 122:
                    yyst[yysp] = 122;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 296:
                    yyn = yys122();
                    continue;

                case 123:
                    yyst[yysp] = 123;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 297:
                    yyn = yys123();
                    continue;

                case 124:
                    yyst[yysp] = 124;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 298:
                    yyn = yys124();
                    continue;

                case 125:
                    yyst[yysp] = 125;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 299:
                    yyn = yys125();
                    continue;

                case 126:
                    yyst[yysp] = 126;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 300:
                    yyn = yys126();
                    continue;

                case 127:
                    yyst[yysp] = 127;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 301:
                    yyn = yys127();
                    continue;

                case 128:
                    yyst[yysp] = 128;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 302:
                    yyn = yys128();
                    continue;

                case 129:
                    yyst[yysp] = 129;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 303:
                    yyn = yys129();
                    continue;

                case 130:
                    yyst[yysp] = 130;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 304:
                    yyn = yys130();
                    continue;

                case 131:
                    yyst[yysp] = 131;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 305:
                    yyn = yys131();
                    continue;

                case 132:
                    yyst[yysp] = 132;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 306:
                    yyn = yys132();
                    continue;

                case 133:
                    yyst[yysp] = 133;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 307:
                    yyn = yys133();
                    continue;

                case 134:
                    yyst[yysp] = 134;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 308:
                    yyn = yys134();
                    continue;

                case 135:
                    yyst[yysp] = 135;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 309:
                    yyn = yys135();
                    continue;

                case 136:
                    yyst[yysp] = 136;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 310:
                    yyn = yys136();
                    continue;

                case 137:
                    yyst[yysp] = 137;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 311:
                    yyn = yys137();
                    continue;

                case 138:
                    yyst[yysp] = 138;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 312:
                    yyn = yys138();
                    continue;

                case 139:
                    yyst[yysp] = 139;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 313:
                    switch (yytok) {
                        case ')':
                            yyn = 161;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 140:
                    yyst[yysp] = 140;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 314:
                    yyn = yys140();
                    continue;

                case 141:
                    yyst[yysp] = 141;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 315:
                    yyn = yys141();
                    continue;

                case 142:
                    yyst[yysp] = 142;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 316:
                    yyn = yys142();
                    continue;

                case 143:
                    yyst[yysp] = 143;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 317:
                    yyn = yys143();
                    continue;

                case 144:
                    yyst[yysp] = 144;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 318:
                    yyn = yys144();
                    continue;

                case 145:
                    yyst[yysp] = 145;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 319:
                    yyn = yys145();
                    continue;

                case 146:
                    yyst[yysp] = 146;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 320:
                    yyn = yys146();
                    continue;

                case 147:
                    yyst[yysp] = 147;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 321:
                    yyn = yys147();
                    continue;

                case 148:
                    yyst[yysp] = 148;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 322:
                    yyn = yys148();
                    continue;

                case 149:
                    yyst[yysp] = 149;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 323:
                    yyn = yys149();
                    continue;

                case 150:
                    yyst[yysp] = 150;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 324:
                    switch (yytok) {
                        case CAND:
                            yyn = 122;
                            continue;
                        case COR:
                        case ';':
                        case ',':
                        case ')':
                            yyn = yyr55();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 151:
                    yyst[yysp] = 151;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 325:
                    yyn = yys151();
                    continue;

                case 152:
                    yyst[yysp] = 152;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 326:
                    yyn = yys152();
                    continue;

                case 153:
                    yyst[yysp] = 153;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 327:
                    yyn = yys153();
                    continue;

                case 154:
                    yyst[yysp] = 154;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 328:
                    yyn = yys154();
                    continue;

                case 155:
                    yyst[yysp] = 155;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 329:
                    yyn = yys155();
                    continue;

                case 156:
                    yyst[yysp] = 156;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 330:
                    yyn = yys156();
                    continue;

                case 157:
                    yyst[yysp] = 157;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 331:
                    yyn = yys157();
                    continue;

                case 158:
                    yyst[yysp] = 158;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 332:
                    yyn = yys158();
                    continue;

                case 159:
                    yyst[yysp] = 159;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 333:
                    yyn = yys159();
                    continue;

                case 160:
                    yyst[yysp] = 160;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 334:
                    yyn = yys160();
                    continue;

                case 161:
                    yyst[yysp] = 161;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 335:
                    yyn = yys161();
                    continue;

                case 162:
                    yyst[yysp] = 162;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 336:
                    switch (yytok) {
                        case ',':
                        case ')':
                            yyn = yyr98();
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 163:
                    yyst[yysp] = 163;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 337:
                    switch (yytok) {
                        case ')':
                            yyn = 168;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 164:
                    yyst[yysp] = 164;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 338:
                    switch (yytok) {
                        case ')':
                            yyn = 169;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 165:
                    yyst[yysp] = 165;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 339:
                    yyn = yys165();
                    continue;

                case 166:
                    yyst[yysp] = 166;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 340:
                    switch (yytok) {
                        case ')':
                            yyn = 171;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 167:
                    yyst[yysp] = 167;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 341:
                    yyn = yys167();
                    continue;

                case 168:
                    yyst[yysp] = 168;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 342:
                    yyn = yys168();
                    continue;

                case 169:
                    yyst[yysp] = 169;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 343:
                    switch (yytok) {
                        case ';':
                            yyn = 172;
                            continue;
                    }
                    yyn = 351;
                    continue;

                case 170:
                    yyst[yysp] = 170;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 344:
                    yyn = yys170();
                    continue;

                case 171:
                    yyst[yysp] = 171;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 345:
                    yyn = yys171();
                    continue;

                case 172:
                    yyst[yysp] = 172;
                    yysv[yysp] = (lexer.getSemantic()
                                 );
                    yytok = (lexer.nextToken()
                            );
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 346:
                    yyn = yys172();
                    continue;

                case 173:
                    yyst[yysp] = 173;
                    if (++yysp>=yyst.length) {
                        yyexpand();
                    }
                case 347:
                    yyn = yys173();
                    continue;

                case 348:
                    return true;
                case 349:
                    yyerror("stack overflow");
                case 350:
                    return false;
                case 351:
                    yyerror("syntax error");
                    return false;
            }
        }
    }

    protected void yyexpand() {
        int[] newyyst = new int[2*yyst.length];
        Object[] newyysv = new Object[2*yyst.length];
        for (int i=0; i<yyst.length; i++) {
            newyyst[i] = yyst[i];
            newyysv[i] = yysv[i];
        }
        yyst = newyyst;
        yysv = newyysv;
    }

    private int yys0() {
        switch (yytok) {
            case ENDINPUT:
            case STATIC:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case ABSTRACT:
                return yyr3();
        }
        return 351;
    }

    private int yys2() {
        switch (yytok) {
            case ENDINPUT:
                return yyr1();
            case STATIC:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case ABSTRACT:
                return yyr18();
        }
        return 351;
    }

    private int yys3() {
        switch (yytok) {
            case ENDINPUT:
            case STATIC:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case ABSTRACT:
                return yyr2();
        }
        return 351;
    }

    private int yys4() {
        switch (yytok) {
            case ABSTRACT:
                return 6;
            case CLASS:
                return 7;
            case PRIVATE:
                return 8;
            case PROTECTED:
                return 9;
            case PUBLIC:
                return 10;
            case STATIC:
                return 11;
        }
        return 351;
    }

    private int yys5() {
        switch (yytok) {
            case VOID:
            case INT:
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case BOOLEAN:
            case ABSTRACT:
                return yyr12();
        }
        return 351;
    }

    private int yys6() {
        switch (yytok) {
            case VOID:
            case INT:
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case BOOLEAN:
            case ABSTRACT:
                return yyr16();
        }
        return 351;
    }

    private int yys8() {
        switch (yytok) {
            case VOID:
            case INT:
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case BOOLEAN:
            case ABSTRACT:
                return yyr14();
        }
        return 351;
    }

    private int yys9() {
        switch (yytok) {
            case VOID:
            case INT:
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case BOOLEAN:
            case ABSTRACT:
                return yyr15();
        }
        return 351;
    }

    private int yys10() {
        switch (yytok) {
            case VOID:
            case INT:
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case BOOLEAN:
            case ABSTRACT:
                return yyr13();
        }
        return 351;
    }

    private int yys11() {
        switch (yytok) {
            case VOID:
            case INT:
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case BOOLEAN:
            case ABSTRACT:
                return yyr17();
        }
        return 351;
    }

    private int yys15() {
        switch (yytok) {
            case VOID:
            case INT:
            case '}':
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case BOOLEAN:
            case ABSTRACT:
                return yyr8();
        }
        return 351;
    }

    private int yys17() {
        switch (yytok) {
            case '(':
            case '&':
            case NEQ:
            case CAND:
            case '{':
            case IDENT:
            case '>':
            case '=':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case ',':
            case '^':
            case '+':
            case '|':
            case '*':
            case ')':
            case EQEQ:
                return yyr22();
        }
        return 351;
    }

    private int yys18() {
        switch (yytok) {
            case '}':
                return 22;
            case VOID:
            case INT:
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case BOOLEAN:
            case ABSTRACT:
                return yyr18();
        }
        return 351;
    }

    private int yys20() {
        switch (yytok) {
            case VOID:
            case INT:
            case '}':
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case BOOLEAN:
            case ABSTRACT:
                return yyr7();
        }
        return 351;
    }

    private int yys21() {
        switch (yytok) {
            case ABSTRACT:
                return 6;
            case PRIVATE:
                return 8;
            case PROTECTED:
                return 9;
            case PUBLIC:
                return 10;
            case STATIC:
                return 11;
            case IDENT:
                return 17;
            case BOOLEAN:
                return 26;
            case INT:
                return 27;
            case VOID:
                return 28;
        }
        return 351;
    }

    private int yys22() {
        switch (yytok) {
            case ENDINPUT:
            case STATIC:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case CLASS:
            case ABSTRACT:
                return yyr4();
        }
        return 351;
    }

    private int yys23() {
        switch (yytok) {
            case '(':
            case '&':
            case NEQ:
            case CAND:
            case '{':
            case IDENT:
            case '>':
            case '=':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case ',':
            case '^':
            case '+':
            case '|':
            case '*':
            case ')':
            case EQEQ:
                return yyr23();
        }
        return 351;
    }

    private int yys34() {
        switch (yytok) {
            case VOID:
            case INT:
            case '}':
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case BOOLEAN:
            case ABSTRACT:
                return yyr9();
        }
        return 351;
    }

    private int yys48() {
        switch (yytok) {
            case VOID:
            case INT:
            case '}':
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case BOOLEAN:
            case ABSTRACT:
                return yyr33();
        }
        return 351;
    }

    private int yys49() {
        switch (yytok) {
            case VOID:
            case INT:
            case '}':
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case BOOLEAN:
            case ABSTRACT:
                return yyr11();
        }
        return 351;
    }

    private int yys50() {
        switch (yytok) {
            case VOID:
            case INT:
            case '}':
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case BOOLEAN:
            case ABSTRACT:
                return yyr32();
        }
        return 351;
    }

    private int yys51() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr47();
        }
        return 351;
    }

    private int yys53() {
        switch (yytok) {
            case VOID:
            case INT:
            case '}':
            case STATIC:
            case IDENT:
            case PUBLIC:
            case PROTECTED:
            case PRIVATE:
            case BOOLEAN:
            case ABSTRACT:
                return yyr10();
        }
        return 351;
    }

    private int yys54() {
        switch (yytok) {
            case IDENT:
                return 17;
            case BOOLEAN:
                return 26;
            case INT:
                return 27;
            case '{':
                return 51;
            case DO:
                return 67;
            case FALSE:
                return 68;
            case IF:
                return 69;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case RETURN:
                return 73;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case WHILE:
                return 77;
            case '(':
                return 78;
            case ';':
                return 79;
            case '}':
                return 80;
        }
        return 351;
    }

    private int yys56() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case ELSE:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr42();
        }
        return 351;
    }

    private int yys59() {
        switch (yytok) {
            case '=':
                return yyr54();
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr84();
        }
        return 351;
    }

    private int yys61() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr80();
        }
        return 351;
    }

    private int yys65() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr48();
        }
        return 351;
    }

    private int yys67() {
        switch (yytok) {
            case IDENT:
                return 17;
            case '{':
                return 51;
            case DO:
                return 67;
            case FALSE:
                return 68;
            case IF:
                return 69;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case RETURN:
                return 73;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case WHILE:
                return 77;
            case '(':
                return 78;
            case ';':
                return 79;
        }
        return 351;
    }

    private int yys68() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr88();
        }
        return 351;
    }

    private int yys70() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr86();
        }
        return 351;
    }

    private int yys72() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr89();
        }
        return 351;
    }

    private int yys73() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
            case ';':
                return 108;
        }
        return 351;
    }

    private int yys75() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr81();
        }
        return 351;
    }

    private int yys76() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr87();
        }
        return 351;
    }

    private int yys78() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys79() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case ELSE:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr34();
        }
        return 351;
    }

    private int yys80() {
        switch (yytok) {
            case '+':
            case CAND:
            case '=':
            case ',':
            case error:
            case '*':
            case ENDINPUT:
            case '>':
            case '|':
            case '-':
            case ')':
            case '!':
            case '&':
            case NEQ:
            case EQEQ:
            case CLASS:
            case EXTENDS:
            case '^':
            case COR:
            case '<':
            case '/':
            case '.':
                return 351;
        }
        return yyr43();
    }

    private int yys81() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case ELSE:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr39();
        }
        return 351;
    }

    private int yys82() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys83() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
            case ')':
                return yyr96();
        }
        return 351;
    }

    private int yys88() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys90() {
        switch (yytok) {
            case '&':
                return 121;
            case CAND:
            case '|':
            case '^':
            case COR:
            case ';':
            case ',':
            case ')':
                return yyr62();
        }
        return 351;
    }

    private int yys94() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr85();
        }
        return 351;
    }

    private int yys95() {
        switch (yytok) {
            case EQEQ:
                return 124;
            case NEQ:
                return 125;
            case '&':
            case CAND:
            case '|':
            case '^':
            case COR:
            case ';':
            case ',':
            case ')':
                return yyr64();
        }
        return 351;
    }

    private int yys97() {
        switch (yytok) {
            case '*':
                return 127;
            case '/':
                return 128;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '^':
            case '>':
            case '<':
            case COR:
            case ';':
            case ',':
            case '+':
            case ')':
            case EQEQ:
                return yyr72();
        }
        return 351;
    }

    private int yys98() {
        switch (yytok) {
            case '.':
                return 19;
            case '(':
                return 83;
            case '=':
                return yyr53();
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr76();
        }
        return 351;
    }

    private int yys99() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr83();
        }
        return 351;
    }

    private int yys100() {
        switch (yytok) {
            case '|':
                return 129;
            case CAND:
            case COR:
            case ';':
            case ',':
            case ')':
                return yyr58();
        }
        return 351;
    }

    private int yys101() {
        switch (yytok) {
            case '.':
                return 84;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr77();
        }
        return 351;
    }

    private int yys102() {
        switch (yytok) {
            case '+':
                return 130;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '^':
            case '>':
            case '<':
            case COR:
            case ';':
            case ',':
            case ')':
            case EQEQ:
                return yyr70();
        }
        return 351;
    }

    private int yys103() {
        switch (yytok) {
            case '<':
                return 131;
            case '>':
                return 132;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '^':
            case COR:
            case ';':
            case ',':
            case ')':
            case EQEQ:
                return yyr67();
        }
        return 351;
    }

    private int yys104() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr75();
        }
        return 351;
    }

    private int yys105() {
        switch (yytok) {
            case '^':
                return 133;
            case CAND:
            case '|':
            case COR:
            case ';':
            case ',':
            case ')':
                return yyr60();
        }
        return 351;
    }

    private int yys106() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys107() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys108() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case ELSE:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr40();
        }
        return 351;
    }

    private int yys110() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys116() {
        switch (yytok) {
            case '(':
                return 143;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '=':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr91();
        }
        return 351;
    }

    private int yys121() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys122() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys123() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys124() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys125() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys126() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case ELSE:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr41();
        }
        return 351;
    }

    private int yys127() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys128() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys129() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys130() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys131() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys132() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys133() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys134() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr84();
        }
        return 351;
    }

    private int yys135() {
        switch (yytok) {
            case '.':
                return 19;
            case '(':
                return 83;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr76();
        }
        return 351;
    }

    private int yys136() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr79();
        }
        return 351;
    }

    private int yys137() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr78();
        }
        return 351;
    }

    private int yys138() {
        switch (yytok) {
            case '(':
                return 160;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '=':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr92();
        }
        return 351;
    }

    private int yys140() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr82();
        }
        return 351;
    }

    private int yys141() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr93();
        }
        return 351;
    }

    private int yys142() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys143() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
            case ')':
                return yyr96();
        }
        return 351;
    }

    private int yys144() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr49();
        }
        return 351;
    }

    private int yys145() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
        }
        return 351;
    }

    private int yys146() {
        switch (yytok) {
            case IDENT:
                return 17;
            case '{':
                return 51;
            case DO:
                return 67;
            case FALSE:
                return 68;
            case IF:
                return 69;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case RETURN:
                return 73;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case WHILE:
                return 77;
            case '(':
                return 78;
            case ';':
                return 79;
        }
        return 351;
    }

    private int yys147() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr90();
        }
        return 351;
    }

    private int yys148() {
        switch (yytok) {
            case EQEQ:
                return 124;
            case NEQ:
                return 125;
            case '&':
            case CAND:
            case '|':
            case '^':
            case COR:
            case ';':
            case ',':
            case ')':
                return yyr63();
        }
        return 351;
    }

    private int yys149() {
        switch (yytok) {
            case '|':
                return 129;
            case CAND:
            case COR:
            case ';':
            case ',':
            case ')':
                return yyr57();
        }
        return 351;
    }

    private int yys151() {
        switch (yytok) {
            case '<':
                return 131;
            case '>':
                return 132;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '^':
            case COR:
            case ';':
            case ',':
            case ')':
            case EQEQ:
                return yyr65();
        }
        return 351;
    }

    private int yys152() {
        switch (yytok) {
            case '<':
                return 131;
            case '>':
                return 132;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '^':
            case COR:
            case ';':
            case ',':
            case ')':
            case EQEQ:
                return yyr66();
        }
        return 351;
    }

    private int yys153() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr73();
        }
        return 351;
    }

    private int yys154() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr74();
        }
        return 351;
    }

    private int yys155() {
        switch (yytok) {
            case '^':
                return 133;
            case CAND:
            case '|':
            case COR:
            case ';':
            case ',':
            case ')':
                return yyr59();
        }
        return 351;
    }

    private int yys156() {
        switch (yytok) {
            case '*':
                return 127;
            case '/':
                return 128;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '^':
            case '>':
            case '<':
            case COR:
            case ';':
            case ',':
            case '+':
            case ')':
            case EQEQ:
                return yyr71();
        }
        return 351;
    }

    private int yys157() {
        switch (yytok) {
            case '+':
                return 130;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '^':
            case '>':
            case '<':
            case COR:
            case ';':
            case ',':
            case ')':
            case EQEQ:
                return yyr68();
        }
        return 351;
    }

    private int yys158() {
        switch (yytok) {
            case '+':
                return 130;
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '^':
            case '>':
            case '<':
            case COR:
            case ';':
            case ',':
            case ')':
            case EQEQ:
                return yyr69();
        }
        return 351;
    }

    private int yys159() {
        switch (yytok) {
            case '&':
                return 121;
            case CAND:
            case '|':
            case '^':
            case COR:
            case ';':
            case ',':
            case ')':
                return yyr61();
        }
        return 351;
    }

    private int yys160() {
        switch (yytok) {
            case IDENT:
                return 17;
            case FALSE:
                return 68;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case '(':
                return 78;
            case '!':
                return 106;
            case '-':
                return 107;
            case ')':
                return yyr96();
        }
        return 351;
    }

    private int yys161() {
        switch (yytok) {
            case IDENT:
                return 17;
            case '{':
                return 51;
            case DO:
                return 67;
            case FALSE:
                return 68;
            case IF:
                return 69;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case RETURN:
                return 73;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case WHILE:
                return 77;
            case '(':
                return 78;
            case ';':
                return 79;
        }
        return 351;
    }

    private int yys165() {
        switch (yytok) {
            case ELSE:
                return 170;
            case '(':
            case INTLIT:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr35();
        }
        return 351;
    }

    private int yys167() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case ELSE:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr37();
        }
        return 351;
    }

    private int yys168() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr94();
        }
        return 351;
    }

    private int yys170() {
        switch (yytok) {
            case IDENT:
                return 17;
            case '{':
                return 51;
            case DO:
                return 67;
            case FALSE:
                return 68;
            case IF:
                return 69;
            case INTLIT:
                return 70;
            case NEW:
                return 71;
            case NULL:
                return 72;
            case RETURN:
                return 73;
            case SUPER:
                return 74;
            case THIS:
                return 75;
            case TRUE:
                return 76;
            case WHILE:
                return 77;
            case '(':
                return 78;
            case ';':
                return 79;
        }
        return 351;
    }

    private int yys171() {
        switch (yytok) {
            case '&':
            case NEQ:
            case CAND:
            case '|':
            case '>':
            case '<':
            case COR:
            case ';':
            case '/':
            case '.':
            case '^':
            case ',':
            case '+':
            case '*':
            case ')':
            case EQEQ:
                return yyr95();
        }
        return 351;
    }

    private int yys172() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case ELSE:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr38();
        }
        return 351;
    }

    private int yys173() {
        switch (yytok) {
            case '(':
            case INTLIT:
            case ELSE:
            case WHILE:
            case INT:
            case TRUE:
            case THIS:
            case IF:
            case DO:
            case SUPER:
            case '{':
            case IDENT:
            case RETURN:
            case FALSE:
            case ';':
            case NULL:
            case '}':
            case NEW:
            case BOOLEAN:
                return yyr36();
        }
        return 351;
    }

    private int yyr1() { // program : classes
        yysp -= 1;
        return 1;
    }

    private int yyr63() { // andExpr : andExpr '&' eqExpr
        { yyrv = new BitAndExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypandExpr();
    }

    private int yyr64() { // andExpr : eqExpr
        yysp -= 1;
        return yypandExpr();
    }

    private int yypandExpr() {
        switch (yyst[yysp-1]) {
            case 133: return 159;
            default: return 90;
        }
    }

    private int yyr96() { // args : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return yypargs();
    }

    private int yyr97() { // args : args1
        { yyrv = Args.reverse(((Args)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return yypargs();
    }

    private int yypargs() {
        switch (yyst[yysp-1]) {
            case 143: return 163;
            case 83: return 113;
            default: return 166;
        }
    }

    private int yyr98() { // args1 : args1 ',' expr
        { yyrv = new Args(((Expression)yysv[yysp-1]), ((Args)yysv[yysp-3])); }
        yysv[yysp-=3] = yyrv;
        return 114;
    }

    private int yyr99() { // args1 : expr
        { yyrv = new Args(((Expression)yysv[yysp-1]), null); }
        yysv[yysp-=1] = yyrv;
        return 114;
    }

    private int yyr52() { // assign : lhs '=' expr
        { yyrv = new AssignExpr(((Position)yysv[yysp-2]), ((LeftHandSide)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        switch (yyst[yysp-1]) {
            case 170: return 55;
            case 161: return 55;
            case 146: return 55;
            case 67: return 55;
            case 54: return 55;
            default: return 91;
        }
    }

    private int yyr43() { // block : '{' stmts '}'
        { yyrv = new Block(((Position)yysv[yysp-3]), Stmts.reverse(((Stmts)yysv[yysp-2]))); }
        yysv[yysp-=3] = yyrv;
        switch (yyst[yysp-1]) {
            case 47: return 48;
            case 44: return 48;
            default: return 56;
        }
    }

    private int yyr32() { // body : ';'
        { yyrv = null; }
        yysv[yysp-=1] = yyrv;
        return yypbody();
    }

    private int yyr33() { // body : block
        yysp -= 1;
        return yypbody();
    }

    private int yypbody() {
        switch (yyst[yysp-1]) {
            case 44: return 49;
            default: return 53;
        }
    }

    private int yyr57() { // cAndExpr : cAndExpr CAND orExpr
        { yyrv = new CondAndExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypcAndExpr();
    }

    private int yyr58() { // cAndExpr : orExpr
        yysp -= 1;
        return yypcAndExpr();
    }

    private int yypcAndExpr() {
        switch (yyst[yysp-1]) {
            case 123: return 150;
            default: return 92;
        }
    }

    private int yyr55() { // cOrExpr : cOrExpr COR cAndExpr
        { yyrv = new CondOrExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return 93;
    }

    private int yyr56() { // cOrExpr : cAndExpr
        yysp -= 1;
        return 93;
    }

    private int yyr93() { // call : name '(' args ')'
        { yyrv = new NameInvocation(((Name)yysv[yysp-4]), ((Args)yysv[yysp-2])); }
        yysv[yysp-=4] = yyrv;
        return yypcall();
    }

    private int yyr94() { // call : primary '.' IDENT '(' args ')'
        { yyrv = new ObjectInvocation(((Expression)yysv[yysp-6]), ((Id)yysv[yysp-4]), ((Args)yysv[yysp-2])); }
        yysv[yysp-=6] = yyrv;
        return yypcall();
    }

    private int yyr95() { // call : SUPER '.' IDENT '(' args ')'
        { yyrv = new SuperInvocation(((Id)yysv[yysp-4]), ((Args)yysv[yysp-2])); }
        yysv[yysp-=6] = yyrv;
        return yypcall();
    }

    private int yypcall() {
        switch (yyst[yysp-1]) {
            case 170: return 57;
            case 161: return 57;
            case 146: return 57;
            case 67: return 57;
            case 54: return 57;
            default: return 94;
        }
    }

    private int yyr4() { // class : modifiers CLASS IDENT extends '{' decls '}'
        { checkClassModifiers(((Modifiers)yysv[yysp-7]));
                                  yyrv = new ClassType(((Modifiers)yysv[yysp-7]), ((Id)yysv[yysp-5]), ((Type)yysv[yysp-4]), ((Decls)yysv[yysp-2])); }
        yysv[yysp-=7] = yyrv;
        return 3;
    }

    private int yyr2() { // classes : classes class
        { addClass(((ClassType)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return 2;
    }

    private int yyr3() { // classes : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return 2;
    }

    private int yyr9() { // decl : modifiers type vardecls ';'
        { checkFieldModifiers(((Modifiers)yysv[yysp-4]));
                                  yyrv = new FieldDecl(((Modifiers)yysv[yysp-4]), ((Type)yysv[yysp-3]),
                                                       VarDecls.reverse(((VarDecls)yysv[yysp-2]))); }
        yysv[yysp-=4] = yyrv;
        return 20;
    }

    private int yyr10() { // decl : modifiers VOID IDENT '(' formals ')' body
        { checkMethodModifiers(((Modifiers)yysv[yysp-7]), ((Block)yysv[yysp-1]));
                                  yyrv = new MethDecl(((Modifiers)yysv[yysp-7]), null, ((Id)yysv[yysp-5]), ((Formals)yysv[yysp-3]), ((Block)yysv[yysp-1])); }
        yysv[yysp-=7] = yyrv;
        return 20;
    }

    private int yyr11() { // decl : modifiers type IDENT '(' formals ')' body
        { checkMethodModifiers(((Modifiers)yysv[yysp-7]), ((Block)yysv[yysp-1]));
                                  yyrv = new MethDecl(((Modifiers)yysv[yysp-7]), ((Type)yysv[yysp-6]),   ((Id)yysv[yysp-5]), ((Formals)yysv[yysp-3]), ((Block)yysv[yysp-1])); }
        yysv[yysp-=7] = yyrv;
        return 20;
    }

    private int yyr7() { // decls : decls decl
        { yyrv = ((Decls)yysv[yysp-1]).link(((Decls)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return 18;
    }

    private int yyr8() { // decls : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return 18;
    }

    private int yyr65() { // eqExpr : eqExpr EQEQ relExpr
        { yyrv = new EqualExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypeqExpr();
    }

    private int yyr66() { // eqExpr : eqExpr NEQ relExpr
        { yyrv = new NotEqualExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypeqExpr();
    }

    private int yyr67() { // eqExpr : relExpr
        yysp -= 1;
        return yypeqExpr();
    }

    private int yypeqExpr() {
        switch (yyst[yysp-1]) {
            case 121: return 148;
            default: return 95;
        }
    }

    private int yyr50() { // expr : assign
        yysp -= 1;
        return yypexpr();
    }

    private int yyr51() { // expr : cOrExpr
        yysp -= 1;
        return yypexpr();
    }

    private int yypexpr() {
        switch (yyst[yysp-1]) {
            case 145: return 164;
            case 142: return 162;
            case 110: return 139;
            case 88: return 119;
            case 82: return 112;
            case 78: return 111;
            case 73: return 96;
            default: return 115;
        }
    }

    private int yyr44() { // exprstmt : call
        yysp -= 1;
        return 58;
    }

    private int yyr45() { // exprstmt : new
        yysp -= 1;
        return 58;
    }

    private int yyr46() { // exprstmt : assign
        yysp -= 1;
        return 58;
    }

    private int yyr5() { // extends : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return 13;
    }

    private int yyr6() { // extends : EXTENDS name
        { yyrv = new NameType(((Name)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return 13;
    }

    private int yyr91() { // field : primary '.' IDENT
        { yyrv = new ObjectAccess(((Expression)yysv[yysp-3]), ((Id)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypfield();
    }

    private int yyr92() { // field : SUPER '.' IDENT
        { yyrv = new SuperAccess(((Id)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypfield();
    }

    private int yypfield() {
        switch (yyst[yysp-1]) {
            case 133: return 134;
            case 132: return 134;
            case 131: return 134;
            case 130: return 134;
            case 129: return 134;
            case 128: return 134;
            case 127: return 134;
            case 125: return 134;
            case 124: return 134;
            case 123: return 134;
            case 122: return 134;
            case 121: return 134;
            case 107: return 134;
            case 106: return 134;
            default: return 59;
        }
    }

    private int yyr31() { // formal : type IDENT
        { yyrv = new Formals(((Type)yysv[yysp-2]),((Id)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        switch (yyst[yysp-1]) {
            case 45: return 52;
            default: return 39;
        }
    }

    private int yyr27() { // formals : formals1
        { yyrv = ((Formals)yysv[yysp-1]); }
        yysv[yysp-=1] = yyrv;
        return yypformals();
    }

    private int yyr28() { // formals : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return yypformals();
    }

    private int yypformals() {
        switch (yyst[yysp-1]) {
            case 35: return 40;
            default: return 43;
        }
    }

    private int yyr29() { // formals1 : formals1 ',' formal
        { yyrv = ((Formals)yysv[yysp-1]).link(((Formals)yysv[yysp-3])); }
        yysv[yysp-=3] = yyrv;
        return 41;
    }

    private int yyr30() { // formals1 : formal
        yysp -= 1;
        return 41;
    }

    private int yyr53() { // lhs : name
        { yyrv = new NameAccess(((Name)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return 60;
    }

    private int yyr54() { // lhs : field
        yysp -= 1;
        return 60;
    }

    private int yyr86() { // literal : INTLIT
        yysp -= 1;
        return 61;
    }

    private int yyr87() { // literal : TRUE
        { yyrv = new BooleanLiteral(((Position)yysv[yysp-1]), true); }
        yysv[yysp-=1] = yyrv;
        return 61;
    }

    private int yyr88() { // literal : FALSE
        { yyrv = new BooleanLiteral(((Position)yysv[yysp-1]), false); }
        yysv[yysp-=1] = yyrv;
        return 61;
    }

    private int yyr89() { // literal : NULL
        { yyrv = new NullLiteral(((Position)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return 61;
    }

    private int yyr12() { // modifiers : pos
        { yyrv = new Modifiers(((Position)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return yypmodifiers();
    }

    private int yyr13() { // modifiers : modifiers PUBLIC
        { setPublic(((Modifiers)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return yypmodifiers();
    }

    private int yyr14() { // modifiers : modifiers PRIVATE
        { setPrivate(((Modifiers)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return yypmodifiers();
    }

    private int yyr15() { // modifiers : modifiers PROTECTED
        { setProtected(((Modifiers)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return yypmodifiers();
    }

    private int yyr16() { // modifiers : modifiers ABSTRACT
        { setAbstract(((Modifiers)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return yypmodifiers();
    }

    private int yyr17() { // modifiers : modifiers STATIC
        { setStatic(((Modifiers)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return yypmodifiers();
    }

    private int yypmodifiers() {
        switch (yyst[yysp-1]) {
            case 2: return 4;
            default: return 21;
        }
    }

    private int yyr73() { // mulExpr : mulExpr '*' unary
        { yyrv = new MulExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypmulExpr();
    }

    private int yyr74() { // mulExpr : mulExpr '/' unary
        { yyrv = new DivExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypmulExpr();
    }

    private int yyr75() { // mulExpr : unary
        yysp -= 1;
        return yypmulExpr();
    }

    private int yypmulExpr() {
        switch (yyst[yysp-1]) {
            case 130: return 156;
            default: return 97;
        }
    }

    private int yyr22() { // name : IDENT
        { yyrv = new Name(((Id)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return yypname();
    }

    private int yyr23() { // name : name '.' IDENT
        { yyrv = new Name(((Name)yysv[yysp-3]), ((Id)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypname();
    }

    private int yypname() {
        switch (yyst[yysp-1]) {
            case 170: return 86;
            case 161: return 86;
            case 160: return 98;
            case 146: return 86;
            case 145: return 98;
            case 143: return 98;
            case 142: return 98;
            case 110: return 98;
            case 88: return 98;
            case 83: return 98;
            case 82: return 98;
            case 78: return 98;
            case 73: return 98;
            case 71: return 89;
            case 67: return 86;
            case 54: return 62;
            case 45: return 24;
            case 36: return 24;
            case 35: return 24;
            case 21: return 24;
            case 14: return 16;
            default: return 135;
        }
    }

    private int yyr90() { // new : NEW name '(' ')'
        { yyrv = new NewExpr(((Position)yysv[yysp-4]), ((Name)yysv[yysp-3])); }
        yysv[yysp-=4] = yyrv;
        switch (yyst[yysp-1]) {
            case 170: return 63;
            case 161: return 63;
            case 146: return 63;
            case 67: return 63;
            case 54: return 63;
            default: return 99;
        }
    }

    private int yyr59() { // orExpr : orExpr '|' xorExpr
        { yyrv = new BitOrExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yyporExpr();
    }

    private int yyr60() { // orExpr : xorExpr
        yysp -= 1;
        return yyporExpr();
    }

    private int yyporExpr() {
        switch (yyst[yysp-1]) {
            case 122: return 149;
            default: return 100;
        }
    }

    private int yyr18() { // pos : /* empty */
        { yyrv = lexer.getPos(); }
        yysv[yysp-=0] = yyrv;
        switch (yyst[yysp-1]) {
            case 66: return 85;
            default: return 5;
        }
    }

    private int yyr80() { // primary : literal
        yysp -= 1;
        return yypprimary();
    }

    private int yyr81() { // primary : THIS
        { yyrv = new This(((Position)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return yypprimary();
    }

    private int yyr82() { // primary : '(' expr ')'
        { yyrv = ((Expression)yysv[yysp-2]); }
        yysv[yysp-=3] = yyrv;
        return yypprimary();
    }

    private int yyr83() { // primary : new
        yysp -= 1;
        return yypprimary();
    }

    private int yyr84() { // primary : field
        yysp -= 1;
        return yypprimary();
    }

    private int yyr85() { // primary : call
        yysp -= 1;
        return yypprimary();
    }

    private int yypprimary() {
        switch (yyst[yysp-1]) {
            case 170: return 64;
            case 161: return 64;
            case 146: return 64;
            case 67: return 64;
            case 54: return 64;
            default: return 101;
        }
    }

    private int yyr71() { // addExpr : addExpr '+' mulExpr
        { yyrv = new AddExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypaddExpr();
    }

    private int yyr72() { // addExpr : mulExpr
        yysp -= 1;
        return yypaddExpr();
    }

    private int yypaddExpr() {
        switch (yyst[yysp-1]) {
            case 132: return 158;
            case 131: return 157;
            default: return 102;
        }
    }

    private int yyr68() { // relExpr : relExpr '<' addExpr
        { yyrv = new LessThanExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yyprelExpr();
    }

    private int yyr69() { // relExpr : relExpr '>' addExpr
        { yyrv = new GreaterThanExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yyprelExpr();
    }

    private int yyr70() { // relExpr : addExpr
        yysp -= 1;
        return yyprelExpr();
    }

    private int yyprelExpr() {
        switch (yyst[yysp-1]) {
            case 125: return 152;
            case 124: return 151;
            default: return 103;
        }
    }

    private int yyr34() { // stmt : ';'
        { yyrv = new Empty(((Position)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return yypstmt();
    }

    private int yyr35() { // stmt : IF '(' expr ')' stmt
        { yyrv = new IfThenElse(((Position)yysv[yysp-5]), ((Expression)yysv[yysp-3]), ((Statement)yysv[yysp-1]), null); }
        yysv[yysp-=5] = yyrv;
        return yypstmt();
    }

    private int yyr36() { // stmt : IF '(' expr ')' stmt ELSE stmt
        { yyrv = new IfThenElse(((Position)yysv[yysp-7]), ((Expression)yysv[yysp-5]), ((Statement)yysv[yysp-3]), ((Statement)yysv[yysp-1])); }
        yysv[yysp-=7] = yyrv;
        return yypstmt();
    }

    private int yyr37() { // stmt : WHILE '(' expr ')' stmt
        { yyrv = new While(((Position)yysv[yysp-5]), ((Expression)yysv[yysp-3]), ((Statement)yysv[yysp-1])); }
        yysv[yysp-=5] = yyrv;
        return yypstmt();
    }

    private int yyr38() { // stmt : DO stmt WHILE '(' expr ')' ';'
        { yyrv = new DoWhile(((Position)yysv[yysp-7]), ((Expression)yysv[yysp-3]), ((Statement)yysv[yysp-6])); }
        yysv[yysp-=7] = yyrv;
        return yypstmt();
    }

    private int yyr39() { // stmt : exprstmt ';'
        { yyrv = new ExprStmt(((Position)yysv[yysp-1]), ((StatementExpr)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return yypstmt();
    }

    private int yyr40() { // stmt : RETURN ';'
        { yyrv = new Return(((Position)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return yypstmt();
    }

    private int yyr41() { // stmt : RETURN expr ';'
        { yyrv = new Return(((Position)yysv[yysp-3]), ((Expression)yysv[yysp-2])); }
        yysv[yysp-=3] = yyrv;
        return yypstmt();
    }

    private int yyr42() { // stmt : block
        yysp -= 1;
        return yypstmt();
    }

    private int yypstmt() {
        switch (yyst[yysp-1]) {
            case 161: return 167;
            case 146: return 165;
            case 67: return 87;
            case 54: return 65;
            default: return 173;
        }
    }

    private int yyr47() { // stmts : /* empty */
        { yyrv = null; }
        yysv[yysp-=0] = yyrv;
        return 54;
    }

    private int yyr48() { // stmts : stmts stmt
        { yyrv = new BlockStatement(((Statement)yysv[yysp-1]),((Stmts)yysv[yysp-2])); }
        yysv[yysp-=2] = yyrv;
        return 54;
    }

    private int yyr49() { // stmts : stmts type pos vardecls ';'
        { yyrv = new LocalVarDecl(((Position)yysv[yysp-3]),((Type)yysv[yysp-4]),
                                                   VarDecls.reverse(((VarDecls)yysv[yysp-2])),((Stmts)yysv[yysp-5])); }
        yysv[yysp-=5] = yyrv;
        return 54;
    }

    private int yyr19() { // type : INT
        { yyrv = Type.INT; }
        yysv[yysp-=1] = yyrv;
        return yyptype();
    }

    private int yyr20() { // type : BOOLEAN
        { yyrv = Type.BOOLEAN; }
        yysv[yysp-=1] = yyrv;
        return yyptype();
    }

    private int yyr21() { // type : name
        { yyrv = new NameType(((Name)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return yyptype();
    }

    private int yyptype() {
        switch (yyst[yysp-1]) {
            case 54: return 66;
            case 21: return 25;
            default: return 42;
        }
    }

    private int yyr76() { // unary : name
        { yyrv = new NameAccess(((Name)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        return yypunary();
    }

    private int yyr77() { // unary : primary
        yysp -= 1;
        return yypunary();
    }

    private int yyr78() { // unary : '-' unary
        { yyrv = new NegExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return yypunary();
    }

    private int yyr79() { // unary : '!' unary
        { yyrv = new NotExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=2] = yyrv;
        return yypunary();
    }

    private int yypunary() {
        switch (yyst[yysp-1]) {
            case 128: return 154;
            case 127: return 153;
            case 107: return 137;
            case 106: return 136;
            default: return 104;
        }
    }

    private int yyr26() { // vardecl : IDENT
        { yyrv = new VarDecls(((Id)yysv[yysp-1])); }
        yysv[yysp-=1] = yyrv;
        switch (yyst[yysp-1]) {
            case 33: return 37;
            default: return 29;
        }
    }

    private int yyr24() { // vardecls : vardecls ',' vardecl
        { yyrv = ((VarDecls)yysv[yysp-1]).link(((VarDecls)yysv[yysp-3])); }
        yysv[yysp-=3] = yyrv;
        return yypvardecls();
    }

    private int yyr25() { // vardecls : vardecl
        yysp -= 1;
        return yypvardecls();
    }

    private int yypvardecls() {
        switch (yyst[yysp-1]) {
            case 25: return 30;
            default: return 117;
        }
    }

    private int yyr61() { // xorExpr : xorExpr '^' andExpr
        { yyrv = new BitXorExpr(((Position)yysv[yysp-2]), ((Expression)yysv[yysp-3]), ((Expression)yysv[yysp-1])); }
        yysv[yysp-=3] = yyrv;
        return yypxorExpr();
    }

    private int yyr62() { // xorExpr : andExpr
        yysp -= 1;
        return yypxorExpr();
    }

    private int yypxorExpr() {
        switch (yyst[yysp-1]) {
            case 129: return 155;
            default: return 105;
        }
    }

    protected String[] yyerrmsgs = {
    };

    private MjcLexer    lexer;
    private ClassType[] classes;
    private int         used = 0;
    private boolean     parsed = false;

    public MjcParser(Handler handler, MjcLexer lexer) {
        super(handler);
        this.lexer = lexer;
        lexer.nextToken();
        parsed = parse();
    }

    private void yyerror(String msg) {
        report(new Failure(lexer.getPos(), msg));
    }

    private void addClass(ClassType cls) {
        if (classes==null) {
            classes = new ClassType[1];
        } else if (used>=classes.length) {
            ClassType[] newClasses = new ClassType[2*classes.length];
            for (int i=0; i<classes.length; i++) {
                 newClasses[i] = classes[i];
            }
            classes = newClasses;
        }
        classes[used++] = cls;
    }

    public ClassType[] getClasses() {
        if (parsed) {
            ClassType[] result = new ClassType[used];
            for (int i=0; i<used; i++) {
                result[i] = classes[i];
            }
            return result;
        } else {
            return null;
        }
    }

    void setPublic(Modifiers mods) {
      dontRepeat (mods, Modifiers.PUBLIC,    "public");
      cantCombine(mods, Modifiers.PRIVATE,   "private and public");
      cantCombine(mods, Modifiers.PROTECTED, "protected and public");
      mods.set(Modifiers.PUBLIC);
    }

    void setPrivate(Modifiers mods) {
      dontRepeat (mods, Modifiers.PRIVATE,   "private");
      cantCombine(mods, Modifiers.PUBLIC,    "public and private");
      cantCombine(mods, Modifiers.PROTECTED, "protected and private");
      cantCombine(mods, Modifiers.ABSTRACT,  "abstract and private");
      mods.set(Modifiers.PRIVATE);
    }

    void setProtected(Modifiers mods) {
      dontRepeat (mods, Modifiers.PROTECTED, "protected");
      cantCombine(mods, Modifiers.PUBLIC,    "public and protected");
      cantCombine(mods, Modifiers.PRIVATE,   "private and protected");
      mods.set(Modifiers.PROTECTED);
    }

    void setAbstract(Modifiers mods) {
      dontRepeat (mods, Modifiers.ABSTRACT,  "abstract");
      cantCombine(mods, Modifiers.PRIVATE,   "private and abstract");
      mods.set(Modifiers.ABSTRACT);
    }

    void setStatic(Modifiers mods) {
      dontRepeat (mods, Modifiers.STATIC,  "static");
      mods.set(Modifiers.STATIC);
    }

    void dontRepeat(Modifiers mods, int flags, String msg) {
      if (mods.includes(flags)) {
        report(new Warning(mods.getPos(),
                           "Repeated use of " + msg + " modifier"));
      }
    }

    void cantCombine(Modifiers mods, int flags, String msg) {
      if (mods.includes(flags)) {
        report(new Failure(mods.getPos(), "Cannot combine " + msg));
      }
    }

    void checkClassModifiers(Modifiers mods) {
      if (mods.includes(Modifiers.PUBLIC
                       |Modifiers.PRIVATE
                       |Modifiers.PROTECTED)) {
        report(new Failure(mods.getPos(), "Illegal class access modifier"));
      }
    }

    void checkFieldModifiers(Modifiers mods) {
      if (mods.includes(Modifiers.ABSTRACT)) {
        report(new Failure(mods.getPos(), "A field cannot be abstract"));
      }
    }

    void checkMethodModifiers(Modifiers mods, Block body) {
      if (mods.includes(Modifiers.ABSTRACT) && body!=null) {
        report(new Failure(mods.getPos(),
               "An abstract method cannot have a body"));
      }
    }

}
