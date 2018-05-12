package fyodor.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ListToMatrixConverter {
        public static <T> List<List<T>> convert(int horizontalSize, List<T> list) {
                List<List<T>> articlesMatrix = new LinkedList<>();
                if (list == null) return articlesMatrix;
                Iterator<T> iterator = list.iterator();

                int i = 0;
                List<T> innerList = new LinkedList<>();
                while (iterator.hasNext()) {
                        if (i % horizontalSize == 0) {
                                innerList = new LinkedList<>();
                                articlesMatrix.add(innerList);
                        }
                        T element = iterator.next();
                        innerList.add(element);
                        i++;
                }
                return articlesMatrix;
        }
}
