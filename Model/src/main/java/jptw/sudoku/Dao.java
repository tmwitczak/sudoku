///////////////////////////////////////////////////////////////////// Package //
package jptw.sudoku;


////////////////////////////////////////////////////////////// Interface: Dao //
public
interface Dao<T>
        extends AutoCloseable {

    //=========================================================== Behaviour ==//
    T read()
        throws FileException, DBException;
    
    void write(T obj)
        throws FileException, DBException;


}


////////////////////////////////////////////////////////////////////////////////

