# Joern

This repository is a maven adaptation of the [joern project](https://github.com/fabsx00/joern) (warning: some parts have been removed).

## Author: 

Fabian Yamaguchi <fabian.yamaguchi@cs.uni-goettingen.de>
           
### Contributors:
           
Alwin Maier <alwin.maier@stud.uni-goettingen.de>


## Introduction
This library allows one to parse a C file under the form of a string from a java program.
An AST, CFG or DDG can then be computed from this parsed file.

## Use
This library is used in this project by the [FileMetrics](https://github.com/electricalwind/FilesMetrics.md) project
    
In order to retrieve the AST of a file the following steps are required (code in Kotlin):

1. create a Walker
    
        class TestASTWalker : ASTWalker() {

            var codeItems: MutableList<ASTNode>

            init {
                codeItems = LinkedList<ASTNode>()
            }

            override fun startOfUnit(ctx: ParserRuleContext, filename: String) {

            }

            override fun endOfUnit(ctx: ParserRuleContext, filename: String) {

            }

            override fun processItem(item: ASTNode, itemStack: Stack<ASTNodeBuilder>) {
                codeItems.add(item)
            }

        }

2. create the parser and walker elements
        
        val parser = ANTLRCModuleParserDriver()
        val walker = TestASTWalker()
        parser.addObserver(walker)  

3. Then lexe your file, note that if a tokenize file is enough, you cat stop after this step
        
        val inputStream = ANTLRInputStream(compatibleFileContent())
        val lex = ModuleLexer(inputStream)
        val token = TokenSubStream(lex)
        
4. If you need the AST, the following step are necessary:
        
        parser.parseAndWalkTokenStream(token)
        val listofNode: MutableList<ASTNode> = walker.codeItems
        
5. To compute the CFG, you first need to iterate over the list of node, and in case it is a function, you can do the following:

        for (node in listnode) {
            if (node is FunctionDef) {
                val cfg = CCFGFactory.convert(node.content)
             }
        }
        
6. In case you want to use UseDef Graph 

        val cfgToUdg = CFGToUDGConverter()
        val udg = cfgToUdg.convert(cfg)

7. you can also add taint source

        cfgToUdg.addTaintSource("foo", 0);
        
8. Once this done, you can access the DDG by doing so:
        
        val udgToDefUse = CFGAndUDGToDefUseCFG();
        val defUseCfg = udgToDefUse.convert(cfg,udg)
        val ddg = DDGCreator().createForDefUseCFG(defUseCfg)
       
        
 
      
## Known Issue
* The library is not handling correctly function containing __ 
* No handling of the NULL keyword
