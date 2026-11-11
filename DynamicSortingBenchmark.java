import java.util.Arrays;
import java.util.Scanner;

public class DynamicSortingBenchmark {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int totalSize = 100_000;
        int batchSize = 7; // إدخال يدوي لدفعات من 7 عناصر كما طلبت

        int[] originalArray = new int[totalSize];

        System.out.println("--- نظام مقارنة خوارزميات الترتيب (مع الإدخال اليدوي) ---");
        System.out.println("الرجاء إدخال أول 7 عناصر يدوياً:");
        
        for (int i = 0; i < batchSize; i++) {
            System.out.print("أدخل العنصر رقم " + (i + 1) + ": ");
            originalArray[i] = scanner.nextInt();
        }

        // تكملة باقي حجم البيانات (100,000) تلقائياً لتفادي الانتظار الطويل
        for (int i = batchSize; i < totalSize; i++) {
            originalArray[i] = (int)(Math.random() * 1000);
        }

        // قائمة الخوارزميات المتاحة (بدون رادكس)
        System.out.println("\nاختر الخوارزمية الأولى من القائمة أدناه:");
        printMenu();
        int choice1 = scanner.nextInt();

        System.out.println("\nاختر الخوارزمية الثانية من القائمة أدناه:");
        printMenu();
        int choice2 = scanner.nextInt();

        System.out.println("\nجاري قياس الأداء للخوارزميتين المختارتين عبر جميع السيناريوهات...");

        // طباعة رأس الجدول
        System.out.println("\n------------------------------------------------------------------------------------------------");
        System.out.printf("%-18s | %-15s | %-15s | %-15s | %-15s%n", 
                "الخوارزمية", "غير مرتبة (Random)", "مرتبة عكسياً", "مرتبة جزئياً", "مرتبة تماماً");
        System.out.println("------------------------------------------------------------------------------------------------");

        // تشغيل المقارنة للخوارزمية الأولى والثانية بناءً على اختيارك
        runBatchBenchmark(getAlgorithmName(choice1), originalArray, batchSize, choice1);
        runBatchBenchmark(getAlgorithmName(choice2), originalArray, batchSize, choice2);

        System.out.println("------------------------------------------------------------------------------------------------");
        System.out.println("تم الانتهاء بنجاح.");
        scanner.close();
    }

    public static void printMenu() {
        System.out.println("1. Selection Sort");
        System.out.println("2. Insertion Sort");
        System.out.println("3. Bubble Sort");
        System.out.println("4. Merge Sort");
        System.out.println("5. Quick Sort");
        System.out.print("أدخل رقم خيارك (1-5): ");
    }

    public static String getAlgorithmName(int choice) {
        switch (choice) {
            case 1: return "Selection Sort";
            case 2: return "Insertion Sort";
            case 3: return "Bubble Sort";
            case 4: return "Merge Sort";
            case 5: return "Quick Sort";
            default: return "Unknown";
        }
    }

    // تشغيل المقارنة والقياس للخوارزمية المختارة على الحالات الأربع
    public static void runBatchBenchmark(String name, int[] original, int batchSize, int algoType) {
        long[] totalTimes = new long[4];

        for (int state = 1; state <= 4; state++) {
            int[] testArray = prepareTestCase(original, state);
            long totalStateTimeNano = 0;

            for (int i = 0; i < testArray.length; i += batchSize) {
                int currentBatchSize = Math.min(batchSize, testArray.length - i);
                int[] batch = new int[currentBatchSize];
                System.arraycopy(testArray, i, batch, 0, currentBatchSize);

                long startTime = System.nanoTime();
                executeSort(batch, algoType);
                long endTime = System.nanoTime();

                totalStateTimeNano += (endTime - startTime);
            }

            totalTimes[state - 1] = totalStateTimeNano / 1000; // التحويل إلى ميكروثانية
        }

        System.out.printf("%-18s | %-15d | %-15d | %-15d | %-15d%n", 
                name, totalTimes[0], totalTimes[1], totalTimes[2], totalTimes[3]);
    }

    // تجهيز السيناريوهات الأربع (الحالات)
    public static int[] prepareTestCase(int[] original, int type) {
        int[] arr = Arrays.copyOf(original, original.length);
        if (type == 1) {
            return arr; // غير مرتبة (Random)
        } else if (type == 2) {
            Arrays.sort(arr);
            reverse(arr); // مرتبة عكسياً
        } else if (type == 3) {
            Arrays.sort(arr);
            int swaps = (int) (arr.length * 0.20);
            for (int i = 0; i < swaps; i++) {
                int idx1 = (int)(Math.random() * arr.length);
                int idx2 = (int)(Math.random() * arr.length);
                int temp = arr[idx1];
                arr[idx1] = arr[idx2];
                arr[idx2] = temp;
            } // مرتبة جزئياً (80%)
        } else if (type == 4) {
            Arrays.sort(arr); // مرتبة تماماً
        }
        return arr;
    }

    public static void reverse(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    // موجه لتنفيذ الخوارزمية حسب اختيار المستخدم الرقمي
    public static void executeSort(int[] arr, int algoType) {
        switch (algoType) {
            case 1: selectionSort(arr); break;
            case 2: insertionSort(arr); break;
            case 3: bubbleSort(arr); break;
            case 4: mergeSort(arr, 0, arr.length - 1); break;
            case 5: quickSort(arr, 0, arr.length - 1); break;
        }
    }

    // --- خوارزميات الترتيب ---
    public static void selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) minIdx = j;
            }
            int temp = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i] = temp;
        }
    }

    public static void insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    public static void mergeSort(int[] arr, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    public static void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (int i = 0; i < n1; ++i) L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j) R[j] = arr[m + 1 + j];
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) arr[k++] = L[i++];
            else arr[k++] = R[j++];
        }
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    public static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }
}
