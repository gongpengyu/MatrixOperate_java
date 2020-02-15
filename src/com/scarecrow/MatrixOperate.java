package com.scarecrow;

/**
 * 功能：用于矩阵间的相关计算
 * 说明:该类中大部分函数都是直接对矩阵进行操作的，如需要保存矩阵，可以使用copyData*/
public class MatrixOperate {

    /**
     * 功能：创建单位矩阵
     * 参数：n,方阵的维数
     * 返回值：对应大小的单位阵*/
    public static double[][] getUnitM(int n){
        if(n < 1){
            return null;
        }else{
            double[][] unitM = new double[n][n];
            for(int i=0;i < unitM.length;i++){
                unitM[i][i] = 1;
            }
            return unitM;
        }
    }

    /**
     * 功能说明：复制矩阵中元素
     * 参数：source，待复制的矩阵
     * 返回值，复制后的矩阵*/
    public static double[][] copyMatrix(double[][] source){
        if(source == null){
            System.out.println("Matrix:null");
            return null;
        }

        double[][] output = new double[source.length][source[0].length];
        for(int i=0;i < source.length;i++)
            for (int j=0;j < source[0].length;j++){
                output[i][j] = source[i][j];
            }
        return output;
    }

    /**
     * 功能说明：进行矩阵之间的加减运算
     * 参数：以a1+a2形式进行计算
     * 返回值：一个新的矩阵，包含计算结果
     */
    public static double[][] addMatrix(double[][] a1,double[][] a2){
        if(a1 == null && a2 == null){
            System.out.println("Matrix:null");
            return null;
        }else{
            //检查矩阵格式是否正确
            if(a1.length != a2.length || a1[0].length != a2[0].length){
                System.out.println("Matrix format problem");
                return null;
            }

            //进行运算
            double[][] newM = new double[a1.length][a1[0].length];
            for(int i=0;i < a1.length;i++)
                for (int j=0;j < a1[0].length;j++){
                    newM[i][j] = a1[i][j] + a2[i][j];
                }
            return newM;
        }
    }

    /**
     * 功能说明：进行矩阵间的乘法运算
     * 参数：a1（ixj） * a2(jxk)
     * 返回值：新矩阵，包含计算结果*/
    public static double[][] multiplyMatrix(double[][] a1,double[][] a2){
        if(a1 == null || a2 == null){
            System.out.println("Matrix : null");
            return null;
        }else{
            //检验格式是否正确
            if(a1[0].length != a2.length){
                System.out.println("Matrix format problem");
                return null;
            }

            //进行运算
            double[][] newM = new double[a1.length][a2[0].length];

            for(int i=0;i < newM.length;i++){
                for(int j=0;j < newM[0].length;j++){

                    //给newM赋值
                    for(int k=0; k < a1[0].length; k++){
                        newM[i][j] += a1[i][k] * a2[k][j];
                    }
                    newM[i][j] = Math.round(newM[i][j]*1000)*0.001;
                }
            }
            return newM;
        }
    }

    /**
     * 功能说明：行列式的计算,不改变输入矩阵
     * 参数：a1(nxn)
     * 返回值：行列式的值*/
    public static double getDet(double[][] a1){
        double[][] matrix = copyMatrix(a1);

        if(matrix == null || matrix.length != matrix[0].length){
            System.out.println("Matrix:format error!");
            return Double.NaN;
        }

        //进行主元素对换
        //思路：遍历第一列，确定最大值，替换到第一行，如此反复
        //i代表行号，j代表列号,flag用于计算行交换后的正负
        int max_j;
        double temp;
        int flag = 1;
        for(int i=0;i < matrix.length;i++){

            //寻找最大数所在列号
            max_j = i;
            for (int j=i; j < matrix[i].length;j++){
                if(matrix[i][max_j] < matrix[i][j]){
                    max_j = j;
                }
            }

            //进行列的交换
            for(int k=0; k < matrix.length;k++){
                temp = matrix[k][i];
                matrix[k][i] = matrix[k][max_j];
                matrix[k][max_j] = temp;
            }

            //判断是否需要乘-1
            if(max_j != i){
                flag *= (-1);
            }
        }

        //进行对角化
        //i代表行号，j代表列号,k代表待处理数据的行号,main_mole代表主元素的分子，deno代表分母
        double main_mole = 0.0;
        double main_deno = 0.0;
        for(int i=0;i < matrix.length;i++){
            for (int k=i+1 ; k < matrix.length;k++){
                main_mole = matrix[k][i];
                main_deno = matrix[i][i];
                for(int j=0;j < matrix[i].length;j++){
                    matrix[k][j] = matrix[k][j] + matrix[i][j] * (-1) * (main_mole / main_deno);
                }
            }
        }

        //计算，返回计算值
        double data_caculate = 1.0;
        for(int i=0;i < matrix.length;i++){
            data_caculate *= matrix[i][i];
        }

        return data_caculate*flag;
    }

    /**
     * 功能说明：矩阵的倒置,不改变原矩阵
     * 参数：a1(ixj)
     * 返回值：转置后的矩阵
     * */
    public static double[][] transportM(double[][] source){
        double[][] a1 = copyMatrix(source);

        if(a1 == null){
            System.out.println("Matrix:null");
            return null;
        }

        //根据对角进行交换元素
        double temp = 0.0;
        for(int i=0;i < a1.length - 1 ;i++)
            for(int j=i+1;j < a1[i].length;j++){
                temp = a1[i][j];
                a1[i][j] = a1[j][i];
                a1[j][i] = temp;
            }
        return a1;
    }

    /**
     * 功能：计算逆矩阵
     * 参数：a1(nxn)
     * 返回值：新的逆矩阵
     * 说明：采用初等变换计算*/
    public static double[][] getInvM(double[][] source){
        double[][] a1 = copyMatrix(source);
        if(a1 == null || a1.length != a1[0].length){
            System.out.println("Matrix:null or format problem");
            return null;
        }

        double det = MatrixOperate.getDet(a1);
        if(det == 0.0 || det == Double.NaN) {
            System.out.println("Matrix:irreversible");
            return null;
        }else{
            double[][] unitM = getUnitM(source.length);
            double temp = 0.0;
            //先行变换成上三角矩阵
            for(int j=0; j < a1[0].length-1;j++){
                for(int i=j+1;i < a1.length;i++){
                    temp = a1[i][j]/a1[j][j];
                    for(int k=0;k < a1[0].length;k++){
                        a1[i][k] = a1[i][k] - a1[j][k]*temp;
                        //四舍五入，保留3位小数
                        a1[i][k] = Math.round(a1[i][k]*1000)*0.001;

                        unitM[i][k] = unitM[i][k] - unitM[j][k]*temp;
                        unitM[i][k] = Math.round(unitM[i][k]*1000)*0.001;
                    }
                }
            }
            //再行变换为对角矩阵
            for(int j=a1[0].length-1;j>0;j--){
                for(int i=j-1;i>=0;i--){
                    temp = a1[i][j]/a1[j][j];
                    for(int k=0;k < a1[0].length;k++){
                        a1[i][k]=a1[i][k] - a1[j][k]*temp;
                        a1[i][k] = Math.round(a1[i][k]*1000)*0.001;

                        unitM[i][k] = unitM[i][k] - unitM[j][k]*temp;
                        unitM[i][k] = Math.round(unitM[i][k]*1000)*0.001;
                    }
                }
            }

            //化为单位矩阵
            for(int i=0;i < a1.length;i++) {
                temp = 1 / a1[i][i];
                for (int k = 0; k < a1[0].length; k++) {
                    a1[i][k] = a1[i][k] * temp;
                    a1[i][k] = Math.round(a1[i][k] * 1000) * 0.001;
                    unitM[i][k] = unitM[i][k] * temp;
                    unitM[i][k] = Math.round(unitM[i][k] * 1000) * 0.001;

                }
            }
            return unitM;
        }
    }

    /**
     * 功能：打印矩阵，用于测试
     * 参数：a1(ixj)
     * 返回值：无*/
    public static void printM(double[][] a1){
        for (double[] a1R :a1){
            for(double num : a1R){
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
//        double test = 23.336445;
//        System.out.println("test:"+Math.round(test*100)*0.01);
//        double[][] a1 = {
//                {1,3,4},
//                {2,4,0}
//        };
//
//        double[][] a2 = {
//                {1},
//                {5}
//        };

        double[][] a1 = {
                {1,3,6},
                {0,1,-3},
                {0,0,1}
        };
        System.out.println("原矩阵前：");
        MatrixOperate.printM(a1);
        System.out.println("逆矩阵后：");
        double[][] invA1 = MatrixOperate.getInvM(a1);
        MatrixOperate.printM(invA1);
        System.out.println("二者相乘：");
        double[][] aXinvM = MatrixOperate.multiplyMatrix(a1,invA1);
        MatrixOperate.printM(aXinvM);

//        double[][] a2 = MatrixOperate.copyMatrix(a1);
//
//        System.out.println("the det is:"+MatrixOperate.getDet(a1));

//        for (double[] a1R :newM){
//            for(double num : a1R){
//                System.out.print(num + " ");
//            }
//            System.out.println();
//        }
    }
}
